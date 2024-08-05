package com.hs.application.room.dto;

import com.hs.persistence.entity.chatroom.ChatType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 채팅방 상세 정보
 */
@Getter
@Setter
public class ChatRoomDetailInfo {

    private Long chatRoomId;
    private String chatRoomName;
    private Long memberId;
    private String memberNickname;
    private Boolean isPrivate;
    private String authCode;
    private List<Participant> participants;
    private List<ChatMessageInfo> recentMessages;
    private LocalDateTime lastMessageAt;

    @Getter
    @Setter
    public static class Participant {

        private Long participantId;
        private Long memberId;
        private String memberNickname;
        private Boolean invitable;

        public Participant(Long participantId, Long memberId, String memberNickname, Boolean invitable) {
            this.participantId = participantId;
            this.memberId = memberId;
            this.memberNickname = memberNickname;
            this.invitable = invitable;
        }
    }

    @Getter
    @Setter
    public static class ChatMessageInfo {
        private ChatType chatType;
        private String message;
        private String senderNickname;
        private Boolean hasFiles;
        private LocalDateTime createdAt;

        public ChatMessageInfo(ChatType chatType, String message, String senderNickname, Boolean hasFiles, LocalDateTime createdAt) {
            this.chatType = chatType;
            this.message = message;
            this.senderNickname = senderNickname;
            this.hasFiles = hasFiles;
            this.createdAt = createdAt;
        }
    }

    public ChatRoomDetailInfo(
            Long chatRoomId,
            String chatRoomName,
            Long memberId,
            String memberNickname,
            Boolean isPrivate,
            List<Participant> participantMap,
            List<ChatMessageInfo> recentMessages,
            LocalDateTime lastMessageAt
    ) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.isPrivate = isPrivate;
        this.participants = participantMap;
        this.recentMessages = recentMessages;
        this.lastMessageAt = lastMessageAt;
    }

}
