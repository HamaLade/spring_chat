package com.hs.application.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatWebSocketService {

    private final RedisTemplate<String, String> redisTemplate;

    public void sendChatMessage(Long memberId, String chatMessage) {

    }

}
