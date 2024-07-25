package com.hs.settings.utils;


import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageRateLimiter {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Instant>> lastMessageTime = new ConcurrentHashMap<>();
    private static final long RATE_LIMIT_MS = 2000; // 2초

    public boolean allowMessage(String roomId, String userId) {
        Instant now = Instant.now();
        ConcurrentHashMap<String, Instant> roomUsers = lastMessageTime.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>());
        Instant lastTime = roomUsers.get(userId);

        if (lastTime == null || now.toEpochMilli() - lastTime.toEpochMilli() >= RATE_LIMIT_MS) {
            roomUsers.put(userId, now);
            return true;
        }
        return false;
    }
}
