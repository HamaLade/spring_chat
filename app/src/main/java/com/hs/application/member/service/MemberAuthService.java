package com.hs.application.member.service;


import com.hs.application.auth.AuthService;
import com.hs.application.member.exception.AuthorizationFailed;
import com.hs.application.member.exception.LoginFailedException;
import com.hs.application.member.exception.SignUpFailedException;
import com.hs.application.member.model.MemberUserDetails;
import com.hs.persistance.entity.member.Member;
import com.hs.persistance.repository.memeber.MemberRepository;
import com.hs.util.jwt.JwtUtils;
import com.hs.util.jwt.MemberJwtProperties;
import com.hs.util.web.HttpServletUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemberAuthService implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberJwtProperties memberJwtProperties;

    public void login(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(LoginFailedException::new);

        if (passwordEncoder.matches(password, member.getPassword())) {

            Instant now = Instant.now();

            generateAccessToken(member, now);
            generateRefreshToken(member, now);

            return;
        }

        throw new LoginFailedException();
    }

    private void generateAccessToken(Member member, Instant instant) {

        long accessTokenExpireTime = instant.plusSeconds(memberJwtProperties.accessTokenExpiredTime).getEpochSecond();

        String accessToken = JwtUtils.addAccessTokenPrefix(Jwts.builder()
                .setHeader(MemberJwtProperties.ACCESS_TOKEN_DEFAULT_HEADER)
                .setClaims(memberJwtProperties.getAccessTokenClaims(member))
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(Instant.ofEpochSecond(accessTokenExpireTime)))
                .signWith(memberJwtProperties.getAccessKey(), SignatureAlgorithm.HS256)
                .compact());

        HttpServletUtils.getHttpServletRequest().setAttribute("Authorization", accessToken);

    }

    private void generateRefreshToken(Member member, Instant instant) {

        long refreshTokenExpireTime = instant.plusSeconds(memberJwtProperties.refreshTokenExpiredTime).getEpochSecond();

        String refreshToken = Jwts.builder()
                .setHeader(MemberJwtProperties.REFRESH_TOKEN_DEFAULT_HEADER)
                .setClaims(memberJwtProperties.getRefreshTokenClaims(member))
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(Instant.ofEpochSecond(refreshTokenExpireTime)))
                .signWith(memberJwtProperties.getRefreshKey(), SignatureAlgorithm.HS512)
                .compact();

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) memberJwtProperties.refreshTokenExpiredTime);
        refreshTokenCookie.setPath("/");
        HttpServletUtils.getHttpServletResponse().addCookie(refreshTokenCookie);
    }

    public void signUp(String loginId, String nickName, String password) {

        // 아이디 중복 검사
        memberRepository.findByLoginId(loginId)
                .ifPresent(member -> {
                    throw new SignUpFailedException("이미 존재하는 아이디입니다.");
                });

        // 닉네임 중복 검사
        memberRepository.findByNickname(nickName)
                .ifPresent(member -> {
                    throw new SignUpFailedException("이미 존재하는 닉네임입니다.");
                });

        // 회원가입
        Member member = new Member(
                loginId
                , nickName
                , passwordEncoder.encode(password)
        );

        memberRepository.save(member);
    }

    public UserDetails authorization(String accessToken) {

        UserDetails userDetails = accessTokenAuthorization(accessToken);
        if (userDetails != null) {
            return userDetails;
        }

        Cookie refreshTokenCookie = JwtUtils.findRefreshTokenCookie();

        if (refreshTokenCookie != null) {
            return refreshTokenAuthorization(refreshTokenCookie.getValue());
        }

        return null;
    }

    public UserDetails accessTokenAuthorization(String accessToken) {
        try {
            Jws<Claims> claims = memberJwtProperties.accessParser.parseClaimsJws(accessToken);
            Member member = memberRepository.findById(Long.parseLong((String) claims.getBody().get(MemberJwtProperties.USER_ID)))
                    .orElseThrow(AuthorizationFailed::new);

            return new MemberUserDetails(member);
        } catch (Exception e) {
            return null;
        }
    }

    public UserDetails refreshTokenAuthorization(String refreshToken) {
        try {
            Jws<Claims> claims = memberJwtProperties.refreshParser.parseClaimsJws(refreshToken);
            Member member = memberRepository.findById(Long.parseLong((String) claims.getBody().get(MemberJwtProperties.USER_ID)))
                    .orElseThrow(AuthorizationFailed::new);

            return new MemberUserDetails(member);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


}
