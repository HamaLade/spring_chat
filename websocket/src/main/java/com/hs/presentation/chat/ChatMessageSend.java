package com.hs.presentation.chat;

import lombok.Getter;

@Getter
public class ChatMessageSend {

    private Long memberId;
    private String textContent;

    public ChatMessageSend(Long memberId, String textContent) {
        this.memberId = memberId;
        this.textContent = textContent;
    }

}
