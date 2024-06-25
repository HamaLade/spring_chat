package com.hs.presentation.member.dto;

import lombok.Getter;

@Getter
public class MemberLoginRequestDto {

    private String loginId;
    private String password;

    public MemberLoginRequestDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
