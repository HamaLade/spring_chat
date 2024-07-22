package com.hs.persistence.repository.room;

import com.hs.application.room.dto.ChatRoomDetailInfo;
import com.hs.persistence.entity.chatroom.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // 채팅방ID에 존재하는 참여자들 조회
    List<Participant> findAllByRoomId(Long roomId);

    // 채팅방에 자신의 memberId를 뺀 다른 inviteable이 true인 참여자들 인원 조회
    List<Participant> findAllByRoomIdAndMemberIdNotAndInvitableIsTrue(Long roomId, Long memberId);

    Optional<Participant> findByRoomIdAndMemberId(Long roomId, Long memberId);

    boolean existsByRoomIdAndMemberId(Long roomId, Long memberId);

    @Query("SELECT new com.hs.application.room.dto.ChatRoomDetailInfo$Participant(" +
            "p.id, p.memberId, m.nickname, p.invitable) " +
            "FROM Room r " +
            "LEFT JOIN Participant p on p.room.id = r.id " +
            "LEFT JOIN Member m ON m.id = p.memberId " +
            "WHERE r.id = :roomId")
    List<ChatRoomDetailInfo.Participant> findParticipantsByRoomId(@Param("roomId") Long roomId);

    @Modifying
    int deleteByRoomIdAndMemberId(Long roomId, Long memberId);

}
