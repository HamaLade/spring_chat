package com.hs.presentation.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChatJoinInfo {

    private String memberNickname;

    public ChatJoinInfo(String memberNickname) {
        this.memberNickname = memberNickname;
    }

}
