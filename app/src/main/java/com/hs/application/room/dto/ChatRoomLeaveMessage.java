package com.hs.application.room.dto;

import lombok.Getter;

@Getter
public class ChatRoomLeaveMessage {

    private Long roomId;
    private String senderNickname;
    private String message;

    public ChatRoomLeaveMessage(Long roomId, String senderNickname, String message) {
        this.roomId = roomId;
        this.senderNickname = senderNickname;
        this.message = message;
    }

    public ChatRoomLeaveMessage() {
    }

}
