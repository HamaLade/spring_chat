package com.hs.chat.domain.dto.login.oauth.chatroom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hs.chat.domain.model.chat.room.enums.RoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRoomCreateRequestDto {

    private String roomName;
    private String roomPassword;
    private RoomType roomType;

}
