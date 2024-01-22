package com.hs.chat.global.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OauthAuthorize {

    GITHUB("github", ""),
    NAVER("naver", ""),
    GOOGLE("google", ""),
    KAKAO("kakao", "https://kauth.kakao.com/oauth/authorize")
    ;

    private final String providerName;
    private final String authorizeUrl;

    OauthAuthorize(String name, String authorizeUrl) {
        this.providerName = name;
        this.authorizeUrl = authorizeUrl;
    }

    public static OauthAuthorize of(String providerName) {
        return Arrays.stream(values())
                .filter(oauthAuthorize -> oauthAuthorize.providerName.equals(providerName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 OauthAuthorize가 없습니다. name=" + providerName));
    }

}
