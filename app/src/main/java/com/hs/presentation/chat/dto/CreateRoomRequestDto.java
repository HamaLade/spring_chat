package com.hs.presentation.chat.dto;

import lombok.Getter;

@Getter
public class CreateRoomRequestDto {

    private String roomName;
    private Boolean isPrivate;

    public CreateRoomRequestDto(String roomName, boolean isPrivate) {
        this.roomName = roomName;
        this.isPrivate = isPrivate;
    }

}
