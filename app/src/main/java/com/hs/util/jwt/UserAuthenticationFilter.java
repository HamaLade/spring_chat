package com.hs.util.jwt;

import com.hs.application.member.service.MemberAuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final MemberAuthService memberAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        UserDetails userDetails = null;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null) {
            String removedAccessTokenPrefix = JwtUtils.removeAccessTokenPrefix(authorizationHeader);
            userDetails = memberAuthService.authorization(removedAccessTokenPrefix);
        } else if (JwtUtils.existsRefreshTokenCookie()) {
            Cookie refreshTokenCookie = JwtUtils.findRefreshTokenCookie();
            userDetails = memberAuthService.refreshTokenAuthorization(refreshTokenCookie.getValue());
        }

        if (userDetails != null) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            ));
        }

        filterChain.doFilter(request, response);
    }
}
