package com.hs.setting.utils.jwt;

import com.hs.setting.utils.web.HttpServletUtils;
import jakarta.servlet.http.Cookie;

import java.util.Arrays;

public class JwtUtils {

    public static Cookie findAccessTokenCookie() {
        Cookie[] cookies = HttpServletUtils.getHttpServletRequest().getCookies();

        if (cookies == null || cookies.length == 0) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(MemberJwtProperties.ACCESS_TOKEN_NAME))
                .findAny()
                .orElse(null);
    }

    public static Cookie findRefreshTokenCookie() {
        Cookie[] cookies = HttpServletUtils.getHttpServletRequest().getCookies();

        if (cookies == null || cookies.length == 0) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(MemberJwtProperties.REFRESH_TOKEN_NAME))
                .findAny()
                .orElse(null);
    }

    public static boolean existsAccessTokenCookie() {
        return findAccessTokenCookie() != null;
    }

    public static boolean existsRefreshTokenCookie() {
        return findRefreshTokenCookie() != null;
    }

}
