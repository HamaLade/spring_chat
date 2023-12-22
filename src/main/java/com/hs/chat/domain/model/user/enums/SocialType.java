package com.hs.chat.domain.model.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {

    GOOGLE("구글", "google"),
    NAVER("네이버", "naver"),
    KAKAO("카카오", "kakao")
    ;

    private final String description;
    private final String socialRegistrationId;

    // socialRegistrationId를 통해 SocialType을 찾는다.
    public static SocialType of(String socialRegistrationId) {
        for (SocialType socialType : SocialType.values()) {
            if (socialType.getSocialRegistrationId().equals(socialRegistrationId)) {
                return socialType;
            }
        }
        return null;
    }

}
