package com.hs.persistence.repository.room;

import com.hs.persistence.entity.chatroom.Chat;
import com.hs.persistence.entity.chatroom.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    // RoomId에 해당하는 최근 50개의 채팅 메세지를 조회
    List<Chat> findTop50ByRoomIdOrderByIdDesc(Long roomId);

    @Modifying
    @Query("DELETE FROM Chat c WHERE c.room = :room")
    void deleteAllByRoom(Room room);

}
