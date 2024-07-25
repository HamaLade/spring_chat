package com.hs.setting.utils.jwt;

import com.hs.application.member.service.MemberAuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final MemberAuthService memberAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        UserDetails userDetails = null;

        try {
            if (JwtUtil.existsAccessTokenCookie()) {
                Cookie accessTokenCookie = JwtUtil.findAccessTokenCookie();
                userDetails = memberAuthService.accessTokenAuthorization(accessTokenCookie.getValue());
            } else if (JwtUtil.existsRefreshTokenCookie()) {
                Cookie refreshTokenCookie = JwtUtil.findRefreshTokenCookie();
                userDetails = memberAuthService.refreshTokenAuthorization(refreshTokenCookie.getValue());
                memberAuthService.generateAccessToken(Long.valueOf(userDetails.getUsername()), Instant.now());
            }

            if (userDetails != null) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                ));
            }
        } catch (Exception e) {
            log.error("UserAuthenticationFilter error", e);
        } finally {
            filterChain.doFilter(request, response);
        }

    }
}
