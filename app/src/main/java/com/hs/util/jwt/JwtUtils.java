package com.hs.util.jwt;

import com.hs.util.web.HttpServletUtils;
import jakarta.servlet.http.Cookie;

import java.util.Arrays;

public class JwtUtils {

    public static String addAccessTokenPrefix(String token) {
        return MemberJwtProperties.ACCESS_TOKEN_PREFIX + token;
    }

    public static String removeAccessTokenPrefix(String token) {
        return token.replace(MemberJwtProperties.ACCESS_TOKEN_PREFIX, "");
    }

    public static Cookie findRefreshTokenCookie() {
        Cookie[] cookies = HttpServletUtils.getHttpServletRequest().getCookies();

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(MemberJwtProperties.REFRESH_TOKEN_NAME))
                .findAny()
                .orElse(null);
    }

    public static boolean existsRefreshTokenCookie() {
        return findRefreshTokenCookie() != null;
    }

}
