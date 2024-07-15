package com.hs.application.room.dto;

import lombok.Getter;

@Getter
public class ChatRoomInfo {

    private Long chatRoomId;
    private String chatRoomName;
    private Boolean isPrivate;
    private Long memberCount;

    public ChatRoomInfo(Long chatRoomId, String chatRoomName, Boolean isPrivate, Long memberCount) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.isPrivate = isPrivate;
        this.memberCount = memberCount;
    }
}
