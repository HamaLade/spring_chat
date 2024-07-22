package com.hs.application.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.presentation.chat.ChatMessageRequest;
import com.hs.presentation.chat.ChatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatWebSocketService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendChatMessage(Long roomId, ChatType chatType, String message, String senderNickname, Boolean hasFiles) {

        ChatMessageRequest chatMessage = new ChatMessageRequest(roomId, chatType, message, senderNickname, hasFiles);
        try {
            kafkaTemplate.send("chat-message", objectMapper.writeValueAsString(chatMessage));
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: {}", e.getMessage());
        }
    }

}
