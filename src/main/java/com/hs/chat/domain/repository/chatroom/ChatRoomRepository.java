package com.hs.chat.domain.repository.chatroom;

import com.hs.chat.domain.model.chat.room.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select cr from ChatRoom cr where cr.isBlocked = false and cr.isDeleted = false order by cr.chatRoomSeq desc")
    List<ChatRoom> findAllAvailableChatRoomsOrderByChatRoomSeqDesc();

    // 모든 채팅방 (blocked, deleted 되지 않은) 최근 생성순으로 조회
    @Query("select cr from ChatRoom cr where cr.isPublic=true and cr.isBlocked = false and cr.isDeleted = false order by cr.chatRoomSeq desc")
    List<ChatRoom> findAllPublicAvailableChatRoomsOrderByChatRoomSeqDesc();

    // 채팅방의 최근 생성 순으로 30개 조회
    List<ChatRoom> findTop30ByOrderByChatRoomSeqDesc();

}
