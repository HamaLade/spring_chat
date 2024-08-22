package com.hs.presentation.chat;

import com.hs.application.websocket.service.ChatWebSocketService;
import com.hs.presentation.chat.dto.*;
import com.hs.settings.utils.MessageRateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatWebSocketService chatWebSocketService;
    private final MessageRateLimiter rateLimiter;
    private final SimpMessagingTemplate messagingTemplate;

    @ResponseBody
    @GetMapping("/")
    public String healthCheck() {
        return "alive";
    }

    @MessageMapping("/chat/message/{roomId}")
    public void handleChatMessage(@DestinationVariable String roomId,
                                  ChatMessageRequest chatMessageRequest,
                                  @Headers StompHeaderAccessor headerAccessor) {
        String userId = getUserId(headerAccessor);

        if (rateLimiter.allowMessage(roomId, userId)) {
            ChatMessageSend chatMessageSend = chatWebSocketService.sendChatMessage(
                    chatMessageRequest.getRoomId(),
                    chatMessageRequest.getChatType(),
                    chatMessageRequest.getMessage(),
                    chatMessageRequest.getSenderNickname(),
                    chatMessageRequest.getHasFiles()
            );
            messagingTemplate.convertAndSend("/subscribe/chat/room/" + roomId, chatMessageSend);
        } else {
            // Rate limit exceeded, send error message to user
            messagingTemplate.convertAndSend("/queue/errors" + userId, "메세지는 2초에 한 번만 보낼 수 있습니다.");
        }
    }

    @MessageMapping("/chat/join/{roomId}")
    @SendTo("/subscribe/chat/join/room/{roomId}")
    public ChatJoinInfo join(ChatJoinInfo message) {
        return message;
    }

    private String getUserId(StompHeaderAccessor headerAccessor) {
        Principal principal = headerAccessor.getUser();
        if (principal != null) {
            return principal.getName();
        }
        // 사용자 식별 실패 시 처리 (예: 로그 기록, 예외 발생 등)
        log.error("Failed to identify user");
        return "unknown";
    }

    @KafkaListener(topics = "chat-room-join", groupId = "socket")
    public void chatRoomJoin(String message) {

        ChatRoomJoinMessage chatRoomJoinMessage = chatWebSocketService.getChatRoomJoinMessage(message);

        if (chatRoomJoinMessage != null) {
            ChatMessageSend chatMessageSend = new ChatMessageSend(
                    ChatType.INVITATION,
                    chatRoomJoinMessage.getSenderNickname(),
                    chatRoomJoinMessage.getMessage()
            );
            messagingTemplate.convertAndSend("/subscribe/chat/room/" + chatRoomJoinMessage.getRoomId(), chatMessageSend);
            messagingTemplate.convertAndSend("/subscribe/chat/join/room/" + chatRoomJoinMessage.getRoomId(), chatRoomJoinMessage);
        }
    }

    @KafkaListener(topics = "chat-room-leave", groupId = "socket")
    public void chatRoomLeave(String message) {

        ChatRoomJoinMessage chatRoomJoinMessage = chatWebSocketService.getChatRoomJoinMessage(message);

        if (chatRoomJoinMessage != null) {
            ChatMessageSend chatMessageSend = new ChatMessageSend(
                    ChatType.LEAVE,
                    chatRoomJoinMessage.getSenderNickname(),
                    chatRoomJoinMessage.getMessage()
            );
            messagingTemplate.convertAndSend("/subscribe/chat/room/" + chatRoomJoinMessage.getRoomId(), chatMessageSend);
            messagingTemplate.convertAndSend("/subscribe/chat/leave/room/" + chatRoomJoinMessage.getRoomId(), chatRoomJoinMessage);
        }
    }

    @MessageMapping("/chat/videocall/request/{roomId}")
    public void handleVideoCallRequest(@Payload VideoCallRequest request, SimpMessageHeaderAccessor headerAccessor) {
        String roomId = headerAccessor.getDestination().split("/")[5];
        messagingTemplate.convertAndSend("/subscribe/chat/videocall/request/" + roomId, request);
    }

    @MessageMapping("/chat/videocall/accept/{roomId}")
    public void handleVideoCallAccept(@Payload VideoCallAccept accept, SimpMessageHeaderAccessor headerAccessor) {
        String roomId = headerAccessor.getDestination().split("/")[5];
        messagingTemplate.convertAndSend("/subscribe/chat/videocall/accept/" + roomId, accept);
    }

    @MessageMapping("/chat/videocall/reject/{roomId}")
    public void handleVideoCallReject(@Payload VideoCallReject reject, SimpMessageHeaderAccessor headerAccessor) {
        String roomId = headerAccessor.getDestination().split("/")[5];
        messagingTemplate.convertAndSend("/subscribe/chat/videocall/reject/" + roomId, reject);
    }

    @MessageMapping("/chat/videocall/offer/{roomId}")
    public void handleVideoCallOffer(@Payload VideoCallOffer offer, SimpMessageHeaderAccessor headerAccessor) {
        String roomId = headerAccessor.getDestination().split("/")[5];
        messagingTemplate.convertAndSend("/subscribe/chat/videocall/offer/" + roomId, offer);
    }

    @MessageMapping("/chat/videocall/answer/{roomId}")
    public void handleVideoCallAnswer(@Payload VideoCallAnswer answer, SimpMessageHeaderAccessor headerAccessor) {
        String roomId = headerAccessor.getDestination().split("/")[5];
        messagingTemplate.convertAndSend("/subscribe/chat/videocall/answer/" + roomId, answer);
    }

    @MessageMapping("/chat/videocall/ice/{roomId}")
    public void handleIceCandidate(@Payload IceCandidate iceCandidate, SimpMessageHeaderAccessor headerAccessor) {
        String roomId = headerAccessor.getDestination().split("/")[5];
        messagingTemplate.convertAndSend("/subscribe/chat/videocall/ice/" + roomId, iceCandidate);
    }

}
