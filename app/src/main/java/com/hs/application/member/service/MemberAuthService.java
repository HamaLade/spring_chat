package com.hs.application.member.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.application.auth.AuthService;
import com.hs.application.member.dto.MemberAuthInfo;
import com.hs.application.member.exception.AuthorizationFailed;
import com.hs.application.member.exception.LoginFailedException;
import com.hs.application.member.exception.LoginInfoNotMatched;
import com.hs.application.member.exception.SignUpFailedException;
import com.hs.application.member.model.MemberUserDetails;
import com.hs.persistence.entity.member.Member;
import com.hs.persistence.repository.memeber.MemberRepository;
import com.hs.setting.utils.jwt.JwtUtils;
import com.hs.setting.utils.jwt.MemberJwtProperties;
import com.hs.setting.utils.jwt.TokenInfo;
import com.hs.setting.utils.web.HttpServletUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

/**
 * 회원 인증 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAuthService implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberJwtProperties memberJwtProperties;

    /**
     * 로그인
     * 로그인 성공 시 accessToken, refreshToken을 발급
     *
     * @param loginId  로그인 아이디
     * @param password 비밀번호
     * @return 성공 시 accessToken, refreshToken을 담은 MemberAuthInfo, 실패 시 LoginInfoNotMatched 예외 발생
     * @throws LoginFailedException 로그인 실패
     */
    public MemberAuthInfo login(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(LoginInfoNotMatched::new);

        if (passwordEncoder.matches(password, member.getPassword())) {

            Instant now = Instant.now();
            TokenInfo accessToken = generateAccessToken(member, now);
            TokenInfo refreshToken = generateRefreshToken(member, now);

            return new MemberAuthInfo(accessToken, refreshToken);
        } else {
            throw new LoginInfoNotMatched();
        }
    }

    /**
     * accessToken 발급
     *
     * @param member  회원 정보
     * @param instant 현재 시간
     * @return tokenInfo
     */
    private TokenInfo generateAccessToken(Member member, Instant instant) {

        long accessTokenExpireTime = instant.plusSeconds(memberJwtProperties.getAccessTokenExpiredTime()).getEpochSecond();

        String accessToken = Jwts.builder()
                .setHeader(MemberJwtProperties.ACCESS_TOKEN_DEFAULT_HEADER)
                .setClaims(memberJwtProperties.getAccessTokenClaims(member))
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(Instant.ofEpochSecond(accessTokenExpireTime)))
                .signWith(memberJwtProperties.getAccessKey(), SignatureAlgorithm.HS256)
                .compact();

        Cookie accessTokenCookie = new Cookie(MemberJwtProperties.ACCESS_TOKEN_NAME, accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge((int) memberJwtProperties.getAccessTokenExpiredTime());
        accessTokenCookie.setPath("/");

        HttpServletUtils.getHttpServletResponse().addCookie(accessTokenCookie);

        return new TokenInfo(accessToken, accessTokenExpireTime);
    }

    /**
     * refreshToken 발급
     *
     * @param member  회원 정보
     * @param instant 현재 시간
     * @return tokenInfo
     */
    private TokenInfo generateRefreshToken(Member member, Instant instant) {

        long refreshTokenExpireTime = instant.plusSeconds(memberJwtProperties.getRefreshTokenExpiredTime()).getEpochSecond();

        String refreshToken = Jwts.builder()
                .setHeader(MemberJwtProperties.REFRESH_TOKEN_DEFAULT_HEADER)
                .setClaims(memberJwtProperties.getRefreshTokenClaims(member))
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(Instant.ofEpochSecond(refreshTokenExpireTime)))
                .signWith(memberJwtProperties.getRefreshKey(), SignatureAlgorithm.HS512)
                .compact();

        Cookie refreshTokenCookie = new Cookie(MemberJwtProperties.REFRESH_TOKEN_NAME, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) memberJwtProperties.getRefreshTokenExpiredTime());
        refreshTokenCookie.setPath("/");
        HttpServletUtils.getHttpServletResponse().addCookie(refreshTokenCookie);

        return new TokenInfo(refreshToken, refreshTokenExpireTime);
    }

    /**
     * 로그아웃
     * access & refreshToken 쿠키 제거
     */
    public void logout() {

        HttpServletResponse response = HttpServletUtils.getHttpServletResponse();

        Cookie accessTokenCookie = new Cookie(MemberJwtProperties.ACCESS_TOKEN_NAME, null);
        // 토큰 생성시의 쿠키속성과 속성들이 일치해야 적용된다
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie(MemberJwtProperties.REFRESH_TOKEN_NAME, null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);

    }

    /**
     * 회원가입
     * 중복 검사 후 회원가입
     *
     * @param loginId  로그인 아이디
     * @param nickName 닉네임
     * @param password 비밀번호
     * @throws SignUpFailedException 회원가입 실패
     * @throws AuthorizationFailed   인증 실패
     * @throws LoginFailedException  로그인 실패
     * @throws LoginInfoNotMatched   로그인 정보 불일치
     */
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

    /**
     * 인증
     * accessToken, refreshToken 중 하나라도 유효하면 인증 성공
     *
     * @return UserDetails (인증 실패 시 null)
     */
    public UserDetails authorization() {

        UserDetails userDetails = null;
        Cookie accessTokenCookie = JwtUtils.findAccessTokenCookie();

        if (accessTokenCookie != null) {
            userDetails = accessTokenAuthorization(accessTokenCookie.getValue());
        }

        if (userDetails != null) {
            return userDetails;
        }

        Cookie refreshTokenCookie = JwtUtils.findRefreshTokenCookie();

        if (refreshTokenCookie != null) {
            userDetails = refreshTokenAuthorization(refreshTokenCookie.getValue());
        }

        if (userDetails != null) {
            memberRepository.findById(Long.valueOf(userDetails.getUsername())).ifPresent(member -> {
                // accessToken 재발급, ifPresent는 get()의 경고를 없애기 위해 사용 (현 상황에서는 동시성 이슈를 제외하면 Member Entity는 위에서 이미 조회했기 때문에 null일 수 없다)
                generateAccessToken(member, Instant.now());
            });
            return userDetails;
        }
        return null;
    }

    /**
     * accessToken 인증
     *
     * @param accessToken accessToken
     * @return UserDetails (인증 실패 시 null)
     */
    public UserDetails accessTokenAuthorization(String accessToken) {
        try {
            Jws<Claims> claims = memberJwtProperties.getAccessParser().parseClaimsJws(accessToken);
            Member member = memberRepository.findById(Long.parseLong((String) claims.getBody().get(MemberJwtProperties.USER_ID)))
                    .orElseThrow(AuthorizationFailed::new);

            return new MemberUserDetails(member);
        } catch (Exception e) {
            log.error("accessTokenAuthorization error", e);
            return null;
        }
    }

    /**
     * refreshToken 인증
     *
     * @param refreshToken refreshToken
     * @return UserDetails (인증 실패 시 null)
     */
    public UserDetails refreshTokenAuthorization(String refreshToken) {
        try {
            Jws<Claims> claims = memberJwtProperties.getRefreshParser().parseClaimsJws(refreshToken);
            Member member = memberRepository.findById(Long.parseLong((String) claims.getBody().get(MemberJwtProperties.USER_ID)))
                    .orElseThrow(AuthorizationFailed::new);

            return new MemberUserDetails(member);
        } catch (Exception e) {
            log.error("refreshTokenAuthorization error", e);
            return null;
        }
    }

    /**
     * UserDetails 로드 (사실상 사용하지 않음)
     *
     * @param username username
     * @return UserDetails
     * @throws UsernameNotFoundException usernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    /**
     * 회원 탈퇴
     * @throws AuthorizationFailed 인증 실패
     */
    public void withdraw() {
        UserDetails userDetails = authorization();
        if (userDetails == null) {
            throw new AuthorizationFailed();
        }
        Member member = memberRepository.findById(Long.valueOf(userDetails.getUsername()))
                .orElseThrow(AuthorizationFailed::new);
        memberRepository.delete(member);
    }

    /**
     * 레디스에 요청자의 Refresh Jwt토큰을 Key 요청자의 memberId를 Value로 하는 데이터를 저장
     */
    public void saveTokenToRedis() {

        MemberUserDetails memberUserDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Cookie refreshTokenCookie = JwtUtils.findRefreshTokenCookie();
        if (refreshTokenCookie != null) {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(refreshTokenCookie.getValue(), memberUserDetails.getUsername());
        }
    }

}
