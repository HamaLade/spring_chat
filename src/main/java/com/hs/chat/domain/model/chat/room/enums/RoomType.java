package com.hs.chat.domain.model.chat.room.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomType {

    PUBLIC("공개방"),
    PRIVATE("비공개방")
    ;

    private final String description;

}
