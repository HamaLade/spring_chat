package com.hs.application.member.service;


import com.hs.application.auth.AuthService;
import com.hs.application.member.exception.LoginFailedException;
import com.hs.application.member.exception.SignUpFailedException;
import com.hs.persistance.entity.member.Member;
import com.hs.persistance.repository.memeber.MemberRepository;
import com.hs.util.jwt.MemberJwtProperties;
import com.hs.util.web.HttpServletUtil;
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

        long accessTokenExpireTime = instant.plusMillis(memberJwtProperties.accessTokenExpiredTime).getEpochSecond();

        String accessToken = Jwts.builder()
                .setHeader(MemberJwtProperties.ACCESS_TOKEN_DEFAULT_HEADER)
                .setClaims(memberJwtProperties.getAccessTokenClaims(member))
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(Instant.ofEpochSecond(accessTokenExpireTime)))
                .signWith(memberJwtProperties.getAccessKey(), SignatureAlgorithm.HS256)
                .compact();

        HttpServletUtil.getHttpServletRequest().setAttribute("Authorization", accessToken);

    }

    private void generateRefreshToken(Member member, Instant instant) {

        long refreshTokenExpireTime = instant.plusMillis(memberJwtProperties.refreshTokenExpiredTime).getEpochSecond();

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
        HttpServletUtil.getHttpServletResponse().addCookie(refreshTokenCookie);
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
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


}
