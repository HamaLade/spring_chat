package com.hs.presentation.auth.dto;

import lombok.Getter;

@Getter
public class PasswordChangeRequestDto {

    private String currentPassword;
    private String newPassword;

}
