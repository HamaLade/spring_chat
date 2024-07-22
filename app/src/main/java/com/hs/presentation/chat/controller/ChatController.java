package com.hs.presentation.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hs.application.room.service.ChatService;
import com.hs.presentation.chat.dto.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @KafkaListener(topics = "chat-message", groupId = "chat")
    public void listen(String chatMessageRequestString) {

        ChatMessageRequestDto chatMessageRequestDto = null;
        try {
            chatMessageRequestDto = objectMapper.readValue(chatMessageRequestString, ChatMessageRequestDto.class);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: {}", e.getMessage());
        }

        chatService.saveChatMessage(
                chatMessageRequestDto.getChatType()
                , chatMessageRequestDto.getMessage()
                , chatMessageRequestDto.getRoomId()
                , chatMessageRequestDto.getSenderNickname()
                , chatMessageRequestDto.getHasFiles()
        );

    }

}
