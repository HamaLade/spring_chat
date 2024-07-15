package com.hs.application.room.service;

import com.hs.application.member.model.MemberUserDetails;
import com.hs.application.room.dto.ChatRoomDetailInfo;
import com.hs.application.room.dto.ChatRoomInfo;
import com.hs.application.room.exception.ChatRoomCreateFailed;
import com.hs.persistence.entity.chatroom.Participant;
import com.hs.persistence.entity.chatroom.Room;
import com.hs.persistence.entity.member.Member;
import com.hs.persistence.repository.memeber.MemberRepository;
import com.hs.persistence.repository.room.ParticipantRepository;
import com.hs.persistence.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 채팅방 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;

    /**
     * 채팅방 생성
     * @param roomName 채팅방 이름
     * @param isPrivate 비공개 여부
     */
    public void createRoom(
            String roomName,
            boolean isPrivate
    ) {

        if (roomRepository.existsByRoomName(roomName)) {
            throw new ChatRoomCreateFailed("이미 존재하는 채팅방 이름입니다.");
        }

        Room newRoom = roomRepository.save(
                new Room(
                        roomName,
                        isPrivate
                )
        );

        // 채팅방 생성시 자동으로 생성 요청자가 참가자로 등록
        MemberUserDetails memberUserDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(Long.parseLong(memberUserDetails.getUsername())).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        Participant firstParticipant = new Participant(
                member.getId(),
                true
        );
        firstParticipant.relationWithRoom(newRoom);
        participantRepository.save(firstParticipant);
    }

    /**
     * 채팅방 방 이름으로 삭제
     * @param roomName 채팅방 이름
     */
    public void deleteRoom(
            String roomName
    ) {
        roomRepository.deleteByRoomName(roomName);
    }

    /**
     * 채팅방 방 ID로 삭제
     * @param roomId 채팅방 ID
     */
    public void deleteRoom(
            Long roomId
    ) {
        roomRepository.deleteById(roomId);
    }

    /**
     * 공개된 채팅방 목록 페이징
     * @param pageable 페이징 정보
     */
    public Page<ChatRoomInfo>  getPublicRoomsPage(Pageable pageable) {
        return roomRepository.findAllPublicChatRoomPageToChatRoomInfo(pageable);
    }

    /**
     * 공개된 채팅방 목록 방 이름 검색 페이징
     * @param roomName 채팅방 이름
     * @param pageable 페이징 정보
     */
    public Page<ChatRoomInfo>  getPublicRoomsPage(String roomName,Pageable pageable) {
        return roomRepository.findAllPublicChatRoomPageToChatRoomInfo(roomName, pageable);
    }

    /**
     * 사용자 ID로 사용자가 참가한 채팅방 목록 페이징
     * @param memberId 참가자 ID
     */
    public Page<ChatRoomInfo> getRoomsByMemberId(Long memberId, Pageable pageable) {
        return roomRepository.findAllByMemberIdToChatRoomInfo(memberId, pageable);
    }

    /**
     * 채팅방 상세 정보 조회
     * @param roomId 채팅방 ID
     * @return 채팅방 상세 정보
     */
    public ChatRoomDetailInfo getRoomDetailInfo(Long roomId) {

        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        List<Participant> participants = participantRepository.findAllByRoomId(roomId);
        // participants의 memberId를 리스트로 추출
        List<Long> memberIds = participants.stream()
                .map(Participant::getMemberId)
                .toList();
        List<Member> members = memberRepository.findAllByIdIn(memberIds);
        // participants의 participant를 key로 하여 participant의 memberId와 같은 Id를 가진 Member를 value로 하는 Map 생성
        List<ChatRoomDetailInfo.Participant> participant = participantRepository.findParticipantsByRoomId(roomId);

        return new ChatRoomDetailInfo(
                room,
                participant
        );
    }

    /**
     * 채팅방 참가
     * @param roomId 채팅방 ID
     */
    public void chatRoomJoin(Long roomId) {
        MemberUserDetails memberUserDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(Long.parseLong(memberUserDetails.getUsername())).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );
        // 채팅방에 참가한 참가자 중 초대가능한(inviteable ture) 참가자가 자신을 제외하고 0명이면 채팅방 삭제
        List<Participant> participants = participantRepository.findAllByRoomIdAndMemberIdNotAndInvitableIsTrue(roomId, member.getId());
        Participant participant = new Participant(
                member.getId(),
                true
        );
        participant.relationWithRoom(room);
        participantRepository.save(participant);
    }

    /**
     * 채팅방 초대
     * @param roomId 채팅방 ID
     *               @param memberId 초대할 사용자 ID
     */
    public void chatRoomInvite(Long roomId, Long memberId) {

        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        if (room.getParticipants().stream()
                .anyMatch(participant -> participant.getMemberId().equals(memberId))
        ) {
            throw new IllegalArgumentException("이미 참가한 사용자입니다.");
        }

        Participant participant = new Participant(
                memberId,
                false
        );
        participant.relationWithRoom(room);
    }

    /**
     * 채팅방 나가기
     * @param roomId
     */
    public void chatRoomOut(Long roomId) {
        MemberUserDetails memberUserDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(Long.parseLong(memberUserDetails.getUsername())).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        // 채팅방에 참가한 참가자 중 초대가능한(inviteable ture) 참가자가 자신을 제외하고 0명이면 채팅방 삭제
        List<Participant> participants = participantRepository.findAllByRoomIdAndMemberIdNotAndInvitableIsTrue(roomId, member.getId());

        if (participants.isEmpty()) {
            deleteRoom(roomId);
        } else {
            participantRepository.deleteByRoomIdAndMemberId(roomId, member.getId());
        }
    }

    /**
     * 채팅방에 내가 있는지 확인
     */
    public boolean isParticipant(Long roomId) {
        MemberUserDetails memberUserDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return participantRepository.findByRoomIdAndMemberId(roomId, Long.valueOf(memberUserDetails.getUsername())).isPresent();
    }

}
