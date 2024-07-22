package com.hs.presentation.chat;

import lombok.Getter;

@Getter
public class ChatMessageRequest {

    private Long roomId;
    private ChatType chatType;
    private String message;
    private String senderNickname;
    private Boolean hasFiles;

    public ChatMessageRequest(Long roomId, ChatType chatType, String message, String senderNickname, Boolean hasFiles) {
        this.roomId = roomId;
        this.chatType = chatType;
        this.message = message;
        this.senderNickname = senderNickname;
        this.hasFiles = hasFiles;
    }


}
