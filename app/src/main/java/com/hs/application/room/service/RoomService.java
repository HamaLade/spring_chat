package com.hs.application.room.service;

import com.hs.application.member.model.MemberUserDetails;
import com.hs.application.room.dto.ChatRoomDetailInfo;
import com.hs.application.room.dto.ChatRoomInfo;
import com.hs.application.room.exception.ChatRoomCreateFailedException;
import com.hs.persistence.entity.chatroom.Chat;
import com.hs.persistence.entity.chatroom.Participant;
import com.hs.persistence.entity.chatroom.Room;
import com.hs.persistence.entity.member.Member;
import com.hs.persistence.repository.memeber.MemberRepository;
import com.hs.persistence.repository.room.ChatRepository;
import com.hs.persistence.repository.room.ParticipantRepository;
import com.hs.persistence.repository.room.RoomRepository;
import com.hs.setting.utils.jwt.JwtUtil;
import com.hs.setting.utils.member.MemberUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 채팅방 서비스
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;
    private final ChatRepository chatRepository;

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
            throw new ChatRoomCreateFailedException("이미 존재하는 채팅방 이름입니다.");
        }

        Room newRoom = roomRepository.save(
                new Room(
                        roomName,
                        isPrivate
                )
        );

        // 채팅방 생성시 자동으로 생성 요청자가 참가자로 등록
        MemberUserDetails memberUserDetails = MemberUtil.getMemberUserDetails();
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
     * @param pageable 페이징 정보
     * @return 채팅방 목록
     */
    public Page<ChatRoomInfo> getJoinedChatRoom(Pageable pageable) {
        MemberUserDetails memberUserDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return roomRepository.findAllByMemberIdToChatRoomInfo(memberUserDetails.getId(), pageable);
    }

    /**
     * 사용자 ID로 사용자가 참가한 채팅방 목록 페이징
     * @param memberId 참가자 ID
     * @param pageable 페이징 정보
     * @return 채팅방 목록
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

        MemberUserDetails memberUserDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(Long.parseLong(memberUserDetails.getUsername())).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        List<ChatRoomDetailInfo.Participant> participant = participantRepository.findParticipantsByRoomId(roomId);
        List<Chat> chatList = chatRepository.findTop50ByRoomIdOrderByIdDesc(roomId);
        // chatList를 반대 순서로 List<ChatMessageInfo>로 변환
        List<ChatRoomDetailInfo.ChatMessageInfo> recentMessages = chatList.stream()
                .map(chat -> new ChatRoomDetailInfo.ChatMessageInfo(
                        chat.getMessage(),
                        chat.getSenderNickname(),
                        chat.getHasFiles(),
                        chat.getCreateDate()
                ))
                .collect(Collectors.toList());
        Collections.reverse(recentMessages);

        Cookie refreshTokenCookie = JwtUtil.findRefreshTokenCookie();

        ChatRoomDetailInfo chatRoomDetailInfo = new ChatRoomDetailInfo(
                room.getId(),
                room.getRoomName(),
                member.getId(),
                member.getNickname(),
                room.getIsPrivate(),
                participant,
                recentMessages
        );
        chatRoomDetailInfo.setAuthCode(refreshTokenCookie.getValue());
        return chatRoomDetailInfo;
    }

    /**
     * 채팅방 참가
     * @param roomId 채팅방 ID
     */
    @Transactional
    public void chatRoomJoin(Long roomId) {
        MemberUserDetails memberUserDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(Long.parseLong(memberUserDetails.getUsername())).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        if (!participantRepository.existsByRoomIdAndMemberId(roomId, member.getId())) {
            Participant participant = new Participant(
                    member.getId(),
                    false
            );
            participant.relationWithRoom(room);
            participantRepository.save(participant);
        }
    }

    /**
     * 채팅방 초대
     * @param roomId 채팅방 ID
     * @param memberNickname 초대할 사용자 닉네임
     */
    @Transactional
    public void chatRoomInvite(Long roomId, String memberNickname) {

        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        Member member = memberRepository.findByNickname(memberNickname).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        if (room.getParticipants().stream().anyMatch(participant -> participant.getMemberId().equals(member.getId()))) {
            throw new IllegalArgumentException("이미 참가한 사용자입니다.");
        }

        Participant participant = new Participant(
                member.getId(),
                false
        );

        participant.relationWithRoom(room);
    }

    /**
     * 채팅방 나가기
     * @param roomId 나가는 채팅방 ID
     */
    @Transactional
    public void chatRoomOut(Long roomId) {
        MemberUserDetails memberUserDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(Long.parseLong(memberUserDetails.getUsername())).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        List<Participant> remainingParticipants = room.getParticipants().stream()
                .filter(participant -> !participant.getMemberId().equals(member.getId()))
                .toList();

        boolean shouldDeleteRoom = remainingParticipants.isEmpty() ||
                (room.getIsPrivate() && remainingParticipants.stream().noneMatch(Participant::getInvitable));

        if (shouldDeleteRoom) {
            chatRepository.deleteAllByRoom(room);
            roomRepository.delete(room);
        } else {
            participantRepository.deleteByRoomIdAndMemberId(room.getId(), member.getId());
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
