package com.hs.settings.config.websocket;

import com.hs.settings.utils.StompHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker //웹 소켓 메시지를 다룰 수 있게 허용
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins(
                        "http://www.spring-chat.com:8083", "https://www.spring-chat.com:8083"
                        , "http://www.spring-chat.com", "https://www.spring-chat.com"
                        , "http://localhost:8083", "https://localhost:8083"
                        , "http://localhost", "https://localhost"
                )
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/subscribe");
        config.setApplicationDestinationPrefixes("/publish");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(1024 * 1024 * 6); // 메시지 최대 크기 6MB
        registry.setSendTimeLimit(1000 * 5); // 5초 이내에 메시지 전송
        registry.setSendBufferSizeLimit(1024 * 1024 * 1); // 버퍼 크기 1MB
    }
}
