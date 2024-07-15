package com.hs.presentation.chat;

import com.hs.application.websocket.service.ChatWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatWebSocketService chatWebSocketService;

    @MessageMapping("/chat/message/{roomId}")
    @SendTo("/subscribe/chat/room/{roomId}")
    public ChatMessageSend greeting(ChatMessageRequest message) throws Exception {
        return new ChatMessageSend(message.getMemberId(), message.getTextContent());
    }
}
