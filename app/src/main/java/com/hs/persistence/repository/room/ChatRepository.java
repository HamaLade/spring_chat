package com.hs.persistence.repository.room;

import com.hs.persistence.entity.chatroom.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    // RoomId에 해당하는 최근 50개의 채팅 메세지를 조회
    List<Chat> findTop50ByRoomIdOrderByIdDesc(Long roomId);

}
