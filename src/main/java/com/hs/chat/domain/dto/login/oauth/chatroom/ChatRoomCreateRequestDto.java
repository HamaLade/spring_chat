package com.hs.chat.domain.dto.login.oauth.chatroom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRoomCreateRequestDto {

    private String roomName;
    private String roomPassword;
    private Boolean isPublic;

}
