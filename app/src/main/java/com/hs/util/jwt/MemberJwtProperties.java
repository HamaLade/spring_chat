package com.hs.util.jwt;

import com.hs.persistance.entity.member.Member;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import java.security.Key;
import java.time.Instant;
import java.util.Map;

@Getter
public final class MemberJwtProperties {

    // token secret, access token HMAC256, refresh token HMAC512
    public final String accessTokenSecret;
    public final String refreshTokenSecret;

    // token key
    public final Key accessKey;
    public final Key refreshKey;

    // token expired time
    public final long accessTokenExpiredTime;
    public final long refreshTokenExpiredTime;

    // Access Token
    public static final String ACCESS_TOKEN_NAME = "Authorization";
    public static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 20;

    // Refresh Token
    public static final String REFRESH_TOKEN_NAME = "refreshToken";
    public static final long REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24 * 7;

    // token claim name
    public static final String USER_ID = "uid";
    public static final String ROLE = "role";
    public static final String EXPIRED = "exp";



    // token default header
    public static final Map<String, Object> ACCESS_TOKEN_DEFAULT_HEADER = Map.of("typ", "JWT", "alg", "HS256");
    public static final Map<String, Object> REFRESH_TOKEN_DEFAULT_HEADER = Map.of("typ", "JWT", "alg", "HS512");

    public MemberJwtProperties(String accessTokenSecret, String refreshTokenSecret, long accessTokenExpiredTime, long refreshTokenExpiredTime) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.accessTokenExpiredTime = accessTokenExpiredTime;
        this.refreshTokenExpiredTime = refreshTokenExpiredTime;
        this.accessKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }

    public Map<String, Object> getAccessTokenClaims(Member member, long accessTokenExpiredTime) {
        return Map.of(
                USER_ID, member.getId(),
                ROLE, member.getRole().name(),
                EXPIRED, accessTokenExpiredTime
        );
    }

    public Map<String, Object> getRefreshTokenClaims(Member member, long accessTokenExpiredTime) {
        return Map.of(
                USER_ID, member.getId(),
                EXPIRED, Instant.now().plusMillis(refreshTokenExpiredTime).getEpochSecond()
        );
    }

}
