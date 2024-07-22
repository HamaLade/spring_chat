package com.hs.presentation.chat.dto;

import com.hs.persistence.entity.chatroom.ChatType;
import lombok.Getter;

@Getter
public class ChatMessageRequestDto {

    private Long roomId;
    private ChatType chatType;
    private String message;
    private String senderNickname;
    private Boolean hasFiles;

    public ChatMessageRequestDto(String message, String senderNickname, Boolean hasFiles) {
        this.message = message;
        this.senderNickname = senderNickname;
        this.hasFiles = hasFiles;
    }

    @Override
    public String toString() {
        return "ChatMessageRequestDto{" +
                "roomId=" + roomId +
                ", chatType=" + chatType +
                ", message='" + message + '\'' +
                ", senderNickname='" + senderNickname + '\'' +
                ", hasFiles=" + hasFiles +
                '}';
    }
}
