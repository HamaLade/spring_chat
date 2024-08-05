package com.hs.application.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.presentation.chat.dto.ChatMessageRequest;
import com.hs.presentation.chat.dto.ChatMessageSend;
import com.hs.presentation.chat.ChatType;
import com.hs.presentation.chat.dto.ChatRoomJoinMessage;
import com.hs.presentation.chat.dto.ChatRoomLeaveMessage;
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

    public ChatMessageSend sendChatMessage(Long roomId, ChatType chatType, String message, String senderNickname, Boolean hasFiles) {
        ChatMessageRequest chatMessage = new ChatMessageRequest(roomId, chatType, message, senderNickname, hasFiles);
        try {
            kafkaTemplate.send("chat-message-save", objectMapper.writeValueAsString(chatMessage));
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: {}", e.getMessage());
        }
        return new ChatMessageSend(chatType, senderNickname, message);
    }

    public ChatRoomJoinMessage getChatRoomJoinMessage(String message) {
        try {
            return objectMapper.readValue(message, ChatRoomJoinMessage.class);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: {}", e.getMessage());
            return null;
        }
    }

    public ChatRoomLeaveMessage getChatRoomLeaveMessage(String message) {
        try {
            return objectMapper.readValue(message, ChatRoomLeaveMessage.class);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: {}", e.getMessage());
            return null;
        }
    }

}
