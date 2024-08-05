package com.hs.application.room.dto;

import lombok.Getter;

@Getter
public class ChatRoomJoinMessage {

    private Long roomId;
    private String senderNickname;
    private String message;

    public ChatRoomJoinMessage(Long roomId, String senderNickname, String message) {
        this.roomId = roomId;
        this.senderNickname = senderNickname;
        this.message = message;
    }

    public ChatRoomJoinMessage() {
    }
}
