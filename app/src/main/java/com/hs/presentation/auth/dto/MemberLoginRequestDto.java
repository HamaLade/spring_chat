package com.hs.presentation.auth.dto;

import com.hs.presentation.auth.valid.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberLoginRequestDto {

    @NotBlank(message = ValidationMessage.LOGIN_ID_NOT_BLANK)
    private final String loginId;

    @NotBlank(message = ValidationMessage.PASSWORD_NOT_BLANK)
    private final String password;

    public MemberLoginRequestDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
