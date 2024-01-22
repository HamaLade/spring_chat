package com.hs.chat.global.service.jwt;

import com.hs.chat.global.common.jwt.JwtProperties;
import com.hs.chat.global.util.HttpServletUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(UserDetails userDetails, Duration expiredAt, boolean isRefreshToken) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), userDetails, isRefreshToken);
    }

    private String makeToken(Date expiry, UserDetails userDetails, boolean isRefreshToken) {
        Date now = new Date();

        String jwt = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(jwtProperties.getSecretKey(), isRefreshToken ? SignatureAlgorithm.HS512 : SignatureAlgorithm.HS256)
                .setClaims(userDetailsToClaims(userDetails))
                .compact();

        if (isRefreshToken) {
            Cookie refreshToken = new Cookie("tmp", jwt);
            HttpServletUtil.getHttpServletResponse().addCookie(refreshToken);
        }

        return jwt;
    }

    private Map<String, String> userDetailsToClaims(UserDetails userDetails) {

        if (userDetails == null)
            return Collections.emptyMap();

        GrantedAuthority grantedAuthority = userDetails.getAuthorities().stream().findFirst().orElse(null);

        return Map.of(
                "id", userDetails.getUsername(),
                "userType", grantedAuthority == null ? "" : grantedAuthority.getAuthority()
        );
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {

        Claims claims = getClaims(token);
        String userType = claims.get("userType", String.class);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(userType));

        return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", authorities), token, authorities);
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
