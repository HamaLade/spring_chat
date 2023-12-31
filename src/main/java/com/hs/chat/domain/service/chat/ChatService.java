package com.hs.chat.domain.service.chat;

import com.hs.chat.domain.model.chat.room.ChatRoom;
import com.hs.chat.domain.model.chat.room.enums.RoomType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private Map<Long, ChatRoom> chatRooms;

    @PostConstruct
    //의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기
    public List<ChatRoom> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }

    //채팅방 하나 불러오기
    public ChatRoom findById(String roomId) {
        return chatRooms.get(roomId);
    }

    //채팅방 생성
    public ChatRoom createRoom(String roomName, String roomPassword, RoomType roomType) {
        ChatRoom chatRoom = ChatRoom.create(roomName, roomPassword, roomType);
        chatRooms.put(chatRoom.getChatRoomSeq(), chatRoom);
        return chatRoom;
    }
}
