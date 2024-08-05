package com.hs.persistence.repository.room;

import com.hs.persistence.entity.chatroom.Chat;
import com.hs.persistence.entity.chatroom.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.room.id = :roomId ORDER BY c.createDate DESC limit 50")
    List<Chat> findTop50ByRoomIdOrderByIdDesc(Long roomId);

    @Modifying
    @Query("DELETE FROM Chat c WHERE c.room = :room")
    void deleteAllByRoom(Room room);

    @Query("SELECT c FROM Chat c WHERE c.room.id = :roomId AND c.createDate < :createDate ORDER BY c.createDate DESC limit 50")
    List<Chat> findTop50ByRoomIdAndCreateDateBeforeOrderByIdDesc(Long roomId, LocalDateTime createDate);

}
