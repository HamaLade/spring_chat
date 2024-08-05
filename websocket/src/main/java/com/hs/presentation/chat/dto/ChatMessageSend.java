package com.hs.presentation.chat.dto;

import com.hs.presentation.chat.ChatType;
import lombok.Getter;

@Getter
public class ChatMessageSend {

    private ChatType chatType;
    private String memberNickname;
    private String textContent;

    public ChatMessageSend(ChatType chatType, String memberNickname, String textContent) {
        this.chatType = chatType;
        this.memberNickname = memberNickname;
        this.textContent = textContent;
    }

}
