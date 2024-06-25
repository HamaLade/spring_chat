package com.hs.application.member.dto;

import com.hs.util.jwt.TokenInfo;
import lombok.Getter;

@Getter
public class MemberAuthInfo {

    private final TokenInfo accessTokenInfo;
    private final TokenInfo refreshTokenInfo;

    public MemberAuthInfo(String accessToken, String refreshToken, long accessTokenExpiredTime, long refreshTokenExpiredTime) {
        this.accessTokenInfo = new TokenInfo(accessToken, accessTokenExpiredTime);
        this.refreshTokenInfo = new TokenInfo(refreshToken, refreshTokenExpiredTime);
    }
}
