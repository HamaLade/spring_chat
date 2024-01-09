package com.hs.chat.domain.service.chat;

import com.hs.chat.domain.model.chat.room.ChatRoom;
import com.hs.chat.domain.model.chat.room.enums.RoomType;
import com.hs.chat.domain.repository.chatroom.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    // 모든 채팅방 생성일기준 내림차순 불러오기
    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAllAvailableChatRoomsOrderByChatRoomSeqDesc();
    }

    public List<ChatRoom> findAllPublicRoom() {
        return chatRoomRepository.findAllPublicAvailableChatRoomsOrderByChatRoomSeqDesc();
    }

    //채팅방 하나 불러오기
    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow();
    }

    //채팅방 생성
    @Transactional
    public ChatRoom createRoom(String roomName, String roomPassword, Boolean isPublic) {

        ChatRoom chatRoom = ChatRoom.create(roomName, roomPassword, isPublic);
        chatRoomRepository.save(chatRoom);

        return chatRoom;
    }
}
