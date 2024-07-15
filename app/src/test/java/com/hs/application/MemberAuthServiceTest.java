package com.hs.application;

import com.hs.application.member.service.MemberAuthService;
import com.hs.persistence.entity.member.Member;
import com.hs.persistence.entity.member.MemberRole;
import com.hs.persistence.repository.memeber.MemberRepository;
import com.hs.setting.utils.jwt.MemberJwtProperties;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static com.hs.setting.utils.jwt.MemberJwtProperties.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("회원 인증 서비스 테스트")
public class MemberAuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberJwtProperties memberJwtProperties;

    @InjectMocks
    private MemberAuthService memberAuthService;

    @Test
    @DisplayName("로그인 테스트")
    void login() { // servlet 이 사용되는 경우에는 Context를 사용하는게 나은 것 같다.
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletRequestAttributes attrs = new ServletRequestAttributes(request, response);
        RequestContextHolder.setRequestAttributes(attrs);
        long memberId = 1L;
        String loginId = "test01";
        String nickName = "test01";
        String password = "test01";
        String accessTokenKey = "accessTokenKeyaccessTokenKeyaccessTokenKey";
        long accessTokenExpiredTime = 300L;
        JwtParser accessParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(accessTokenKey.getBytes())).build();
        Map<String, Object> accessTokenClaims = Map.of(
                USER_ID, String.valueOf(memberId),
                ROLE, MemberRole.ROLE_USER.name(),
                EXPIRED, String.valueOf(Instant.now().plusMillis(accessTokenExpiredTime).getEpochSecond())
        );
        String refreshTokenKey = "refreshTokenKeyrefreshTokenKeyrefreshTokenKeyrefreshTokenKeyrefreshTokenKeyrefreshTokenKey";
        long refreshTokenExpiredTime = 600L;
        JwtParser refreshParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(refreshTokenKey.getBytes())).build();
        Map<String, Object> refreshTokenClaims = Map.of(
                USER_ID, String.valueOf(memberId),
                EXPIRED, String.valueOf(Instant.now().plusMillis(accessTokenExpiredTime).getEpochSecond())
        );
        Member member = new Member(memberId, loginId, nickName, password);

        Mockito.lenient().when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        Mockito.when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.of(member));
        Mockito.when(passwordEncoder.matches(password, password)).thenReturn(true);
        Mockito.when(memberJwtProperties.getAccessTokenExpiredTime()).thenReturn(accessTokenExpiredTime);
        Mockito.when(memberJwtProperties.getAccessKey()).thenReturn(Keys.hmacShaKeyFor(accessTokenKey.getBytes()));
        Mockito.when(memberJwtProperties.getAccessTokenClaims(member)).thenReturn(accessTokenClaims);
        Mockito.lenient().when(memberJwtProperties.getAccessParser()).thenReturn(accessParser);
        Mockito.when(memberJwtProperties.getRefreshTokenExpiredTime()).thenReturn(refreshTokenExpiredTime);
        Mockito.when(memberJwtProperties.getRefreshKey()).thenReturn(Keys.hmacShaKeyFor(refreshTokenKey.getBytes()));
        Mockito.lenient().when(memberJwtProperties.getRefreshParser()).thenReturn(refreshParser);
        Mockito.lenient().when(memberJwtProperties.getRefreshTokenClaims(member)).thenReturn(refreshTokenClaims);

        memberAuthService.login(loginId, password);

        Cookie[] cookies = response.getCookies();
        request.setCookies(cookies);

        // 토큰 인증 과정을 거쳐 토큰정보에 있는 memberId와 member의 id가 같은지 확인
        UserDetails authorization = memberAuthService.authorization();

        Assertions.assertEquals(Long.parseLong(authorization.getUsername()), memberId);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signup() {
        String loginId = "test01";
        String nickName = "test01";
        String password = "test01";

        Mockito.when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.empty());
        Mockito.when(memberRepository.findByNickname(nickName)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);

        Assertions.assertDoesNotThrow(() -> memberAuthService.signUp(loginId, nickName, password));
    }


}
