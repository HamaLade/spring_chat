package com.hs.presentation.auth.dto;

import com.hs.presentation.auth.valid.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberSignUpRequestDto {

    @NotBlank(message = ValidationMessage.LOGIN_ID_NOT_BLANK)
    private final String loginId;

    @NotBlank(message = ValidationMessage.NICKNAME_NOT_BLANK)
    private final String nickname;

    @NotBlank(message = ValidationMessage.PASSWORD_NOT_BLANK)
    private final String password;

    public MemberSignUpRequestDto(String loginId, String nickname, String password) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.password = password;
    }
}
