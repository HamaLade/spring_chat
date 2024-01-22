package com.hs.chat.global.common.oauth;

import com.hs.chat.domain.model.user.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfile {

    private final String oauthId;
    private final String email;
    private final String name;
    private final String nickname;
    private final String imageUrl;

    @Builder
    public UserProfile(String oauthId, String email, String name, String nickname, String imageUrl) {
        this.oauthId = oauthId;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public Member toMember() {
        return Member.builder()
                .oauthId(oauthId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .imageUrl(imageUrl)
                .build();
    }

}
