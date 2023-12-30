package com.hs.chat.domain.dto.login.oauth.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hs.chat.domain.enums.chat.ChatType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessageAdd {

    private Long sendUserSeq;
    private ChatType chatType;
    private String message;
    private Long chatRoomSeq;

    public String setMessage(String message) {
        return this.message = message;
    }

}
