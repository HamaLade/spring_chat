package com.hs.setting.utils.jwt;

import com.hs.persistence.entity.member.Member;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
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

    public final JwtParser accessParser;
    public final JwtParser refreshParser;

    // Access Token
    public static final String ACCESS_TOKEN_NAME = "accessToken";
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
        this.accessParser = Jwts.parserBuilder().setSigningKey(accessKey).build();
        this.refreshParser = Jwts.parserBuilder().setSigningKey(refreshKey).build();
    }

    public Map<String, Object> getAccessTokenClaims(Member member) {
        return Map.of(
                USER_ID, member.getId().toString(),
                ROLE, member.getRole().name(),
                EXPIRED, String.valueOf(Instant.now().plusMillis(getRefreshTokenExpiredTime()).getEpochSecond())
        );
    }

    public Map<String, Object> getRefreshTokenClaims(Member member) {
        return Map.of(
                USER_ID, member.getId().toString(),
                EXPIRED, Instant.now().plusMillis(getRefreshTokenExpiredTime()).getEpochSecond()
        );
    }

}
