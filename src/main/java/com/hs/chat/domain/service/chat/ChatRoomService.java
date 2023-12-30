package com.hs.chat.domain.service.chat;

import com.hs.chat.domain.dto.login.oauth.chat.ChatMessageAdd;
import com.hs.chat.domain.model.chat.message.ChatMessage;
import com.hs.chat.domain.model.chat.room.ChatRoom;
import com.hs.chat.domain.repository.chat.ChatMessageRepository;
import com.hs.chat.domain.repository.chatroom.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoom getChatRoom(Long chatRoomSeq) {
        return chatRoomRepository.findById(chatRoomSeq).orElseThrow();
    }

    @Transactional
    public void addChatMessageToChatRoom(ChatRoom chatRoom, ChatMessageAdd chatMessageAdd) {
        ChatMessage addMessage = ChatMessage.builder()
                .chatRoomSeq(chatRoom.getChatRoomSeq())
                .userSeq(chatMessageAdd.getSendUserSeq())
                .message(chatMessageAdd.getMessage())
                .chatType(chatMessageAdd.getChatType())
                .build();

        chatMessageRepository.save(addMessage);
    }

}
