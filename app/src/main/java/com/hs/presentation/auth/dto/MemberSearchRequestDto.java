package com.hs.presentation.auth.dto;

import com.hs.presentation.auth.valid.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberSearchRequestDto {

    @NotBlank(message = ValidationMessage.SEARCH_NICKNAME_NOT_BLANK)
    String memberNickname;

}
