package com.hs.chat.domain.model.user.enums;

import com.hs.chat.domain.exception.oauth.UnsupportedRegistration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum OauthType {

    GOOGLE("구글", "google"),
    NAVER("네이버", "naver"),
    KAKAO("카카오", "kakao")
    ;

    private final String description;
    private final String socialRegistrationId;

    // socialRegistrationId를 통해 SocialType을 찾는다.
    public static OauthType of(String socialRegistrationId) {
        for (OauthType socialType : OauthType.values()) {
            if (socialType.getSocialRegistrationId().equals(socialRegistrationId)) {
                return socialType;
            }
        }
        log.info("기타 로그인 요청");
        // 지원하지 않는 로그인 예외 발생
        throw new UnsupportedRegistration("지원하지 않는 로그인입니다.");
    }

}
