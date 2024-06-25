package com.hs.util.jwt;

import lombok.Getter;

@Getter
public class TokenInfo {

    private final String token;
    private final long expiredTime;

    public TokenInfo(String token, long expiredTime) {
        this.token = token;
        this.expiredTime = expiredTime;
    }

}
