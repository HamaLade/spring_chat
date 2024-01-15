package com.hs.chat.global.handler;

import com.hs.chat.domain.model.user.enums.SocialType;
import com.hs.chat.domain.model.user.member.Member;
import com.hs.chat.domain.service.login.oauth2.MemberService;
import com.hs.chat.global.enums.UserType;
import com.hs.chat.global.model.jwt.JwtToken;
import com.hs.chat.global.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.hs.chat.global.repository.jwt.JwtTokenRepository;
import com.hs.chat.global.service.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/articles";

    private final TokenProvider tokenProvider;
    private final JwtTokenRepository jwtTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registerId = oAuth2User.getAttributes().get("registerId").toString();
        String registrationId = oAuth2User.getAttributes().get("registrationId").toString();
        Member member = memberService.findBySocialId(SocialType.of(registrationId), registerId);
        String refreshToken = tokenProvider.generateToken(member.toUser(), REFRESH_TOKEN_DURATION);

//        saveRefreshToken(user.getId(), refreshToken);
//        addRefreshTokenToCookie(request, response, refreshToken);

//        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
//        String targetUrl = getTargetUrl(accessToken);
//
//        clearAuthenticationAttributes(request, response);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

//    private void saveRefreshToken(Long userSeq, String newRefreshToken) {
//        JwtToken refreshToken = jwtTokenRepository.findByUserId(userSeq)
//                .map(entity -> entity.update(newRefreshToken))
//                .orElse(new JwtToken(userSeq, newRefreshToken));
//
//        jwtTokenRepository.save(refreshToken);
//    }
//
//    // 생성된 리프레시 토큰을 쿠키에 저장
//    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
//        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
//
//        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
//        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
//    }

    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // 액세스 토큰을 패스에 추가
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
