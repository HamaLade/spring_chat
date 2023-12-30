package com.hs.chat.domain.controller.login.message;

import com.hs.chat.domain.dto.login.oauth.chat.ChatMessageAdd;
import com.hs.chat.domain.enums.chat.ChatType;
import com.hs.chat.domain.model.chat.room.ChatRoom;
import com.hs.chat.domain.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/message")
    public void enter(ChatMessageAdd chatMessageAdd) {

        String message = null;
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatMessageAdd.getChatRoomSeq());

        if (chatRoom == null) {
            return;
        }

        if (ChatType.ENTER.equals(chatMessageAdd.getChatType())) {
            message = chatMessageAdd.getSendUserSeq() + "님이 입장하였습니다.";
        } else if (ChatType.QUIT.equals(chatMessageAdd.getChatType())) {
            message = chatMessageAdd.getSendUserSeq() + "님이 퇴장하였습니다.";
        } else {
            message = chatMessageAdd.getMessage();
        }

        chatRoomService.addChatMessageToChatRoom(chatRoom, chatMessageAdd);

        sendingOperations.convertAndSend("/topic/chat/room/" + chatMessageAdd.getChatRoomSeq(), message);
    }
}
