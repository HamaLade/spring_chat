package com.hs.chat.domain.enums.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatType {

    ENTER("ENTER"),
    TALK("TALK"),
    ATTACH("ATTACH"),
    QUIT("QUIT");

    private final String type;

}
