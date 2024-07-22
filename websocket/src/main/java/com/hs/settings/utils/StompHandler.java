package com.hs.settings.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = extractTokenFromCookie(accessor);

            if (token != null) {
                String value = redisTemplate.opsForValue().get(token);
                if (value != null) {
                    accessor.setUser(new Principal() {
                        @Override
                        public String getName() {
                            return value;
                        }
                    });
                    redisTemplate.delete(token);
                } else {
                    log.error("소켓 연결 실패: token: {}", token);
                }
            }
        }

        return message;
    }

    private String extractTokenFromCookie(StompHeaderAccessor accessor) {
        List<String> cookieList = accessor.getNativeHeader("Cookie");
        if (cookieList != null && !cookieList.isEmpty()) {
            for (String cookie : cookieList) {
                String[] cookies = cookie.split(";");
                for (String c : cookies) {
                    String[] cookieParts = c.trim().split("=");
                    if (cookieParts.length == 2 && "refreshToken".equals(cookieParts[0])) {
                        return cookieParts[1];
                    }
                }
            }
        }
        return null;
    }

}
