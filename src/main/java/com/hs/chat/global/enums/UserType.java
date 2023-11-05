package com.hs.chat.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {

    ADMIN("관리자"),
    USER("사용자")
    ;

    private final String description;

}
