package com.hs.chat.global.enums;

import com.hs.chat.global.common.oauth.UserProfile;

import java.util.Arrays;
import java.util.Map;

public enum OauthAttributes {
    GITHUB("github") {
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            return UserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("id")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("avatar_url"))
                    .build();
        }
    },
    NAVER("naver") {
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return UserProfile.builder()
                    .oauthId((String) response.get("id"))
                    .email((String) response.get("email"))
                    .name((String) response.get("name"))
                    .imageUrl((String) response.get("profile_image"))
                    .build();
        }
    },
    GOOGLE("google") {
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            return UserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("sub")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("picture"))
                    .build();
        }
    },
    KAKAO("kakao") {
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            return UserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("id")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("profile_nickname"))
                    .imageUrl((String) attributes.get("profile_image"))
                    .build();
        }
    };

    private final String providerName;

    OauthAttributes(String name) {
        this.providerName = name;
    }

    public static UserProfile extract(String providerName, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(attributes);
    }

    public abstract UserProfile of(Map<String, Object> attributes);

}