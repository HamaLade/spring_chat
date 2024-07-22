package com.hs.persistence.repository.room;

import com.hs.application.room.dto.ChatRoomInfo;
import com.hs.persistence.entity.chatroom.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // 참가자 ID로 채팅방 조회
    @Query("select p.room from Participant p where p.id = :participantId")
    Page<Room> findAllByParticipants(Long participantId, Pageable pageable);

    // 최근 10개 채팅방 조회
    List<Room> findTop10ByOrderByIdDesc();

    // 채팅방 이름으로 조회
    @Query("select r from Room r where r.roomName like %:roomName%")
    List<Room> findAllByRoomName(String roomName, Pageable pageable);

    boolean existsByRoomName(String roomName);

    // 채팅방 이름으로 삭제
    @Modifying
    @Query("delete from Room r where r.roomName = :roomName")
    void deleteByRoomName(String roomName);

    // 공개된 채팅방 페이징
    @Query("select r from Room r where r.isPrivate=false")
    Page<Room> findAllByIsPrivateFalse(Pageable pageable);

    // 공개된 채팅방 페이징
    @Query("SELECT new com.hs.application.room.dto.ChatRoomInfo(r.id, r.roomName, r.isPrivate, " +
            "COUNT(p)) " +
            "FROM Room r INNER JOIN Participant p on r=p.room " +
            "WHERE r.isPrivate = false " +
            "GROUP BY r.id, r.roomName, r.isPrivate " +
            "ORDER BY r.id DESC")
    Page<ChatRoomInfo> findAllPublicChatRoomPageToChatRoomInfo(Pageable pageable);

    // 공개된 채팅방 방 이름 검색 페이징
    @Query("SELECT new com.hs.application.room.dto.ChatRoomInfo(r.id, r.roomName, r.isPrivate, " +
            "COUNT(p)) " +
            "FROM Room r INNER JOIN Participant p on r=p.room " +
            "WHERE r.isPrivate = false " +
            "AND r.roomName LIKE %:roomName% " +
            "GROUP BY r.id, r.roomName, r.isPrivate " +
            "ORDER BY r.id DESC")
    Page<ChatRoomInfo> findAllPublicChatRoomPageToChatRoomInfo(String roomName, Pageable pageable);

    // memberId를 통해 해당 member가 참여한 페이지를 ChatRoomInfo로 변환
    @Query("SELECT new com.hs.application.room.dto.ChatRoomInfo(r.id, r.roomName, r.isPrivate, " +
            "COUNT(p)) " +
            "FROM Room r INNER JOIN Participant p on r=p.room " +
//            "WHERE r IN (SELECT p.room FROM Participant p WHERE p.id = :memberId) " +
            "WHERE p.memberId = :memberId " +
            "GROUP BY r.id, r.roomName, r.isPrivate " +
            "ORDER BY r.id DESC")
    Page<ChatRoomInfo> findAllByMemberIdToChatRoomInfo(Long memberId, Pageable pageable);


}
