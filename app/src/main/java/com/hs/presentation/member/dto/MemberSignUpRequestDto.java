package com.hs.presentation.member.dto;

import lombok.Getter;

@Getter
public class MemberSignUpRequestDto {

    private String loginId;
    private String nickname;
    private String password;

    public MemberSignUpRequestDto(String loginId, String nickname, String password) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.password = password;
    }
}
