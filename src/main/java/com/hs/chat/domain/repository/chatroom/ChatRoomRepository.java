package com.hs.chat.domain.repository.chatroom;

import com.hs.chat.domain.model.chat.room.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
