package com.hs.presentation.chat;

import com.hs.application.websocket.service.ChatWebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatWebSocketService chatWebSocketService;

    @MessageMapping("/chat/message/{roomId}")
    @SendTo("/subscribe/chat/room/{roomId}")
    public ChatMessageSend greeting(ChatMessageRequest chatMessageRequest) throws Exception {
        chatWebSocketService.sendChatMessage(
                chatMessageRequest.getRoomId()
                , chatMessageRequest.getChatType()
                , chatMessageRequest.getMessage()
                , chatMessageRequest.getSenderNickname()
                , chatMessageRequest.getHasFiles()
        );
        return new ChatMessageSend(chatMessageRequest.getChatType(), chatMessageRequest.getSenderNickname(), chatMessageRequest.getMessage());
    }

    @MessageMapping("/chat/join/{roomId}")
    @SendTo("/subscribe/chat/join/room/{roomId}")
    public ChatJoinInfo join(ChatJoinInfo message) throws Exception {
        return message;
    }

}
