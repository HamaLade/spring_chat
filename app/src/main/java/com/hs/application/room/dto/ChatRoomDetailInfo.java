package com.hs.application.room.dto;

import com.hs.persistence.entity.chatroom.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 채팅방 상세 정보
 */
@Getter
@Setter
public class ChatRoomDetailInfo {

    private Long chatRoomId;
    private String chatRoomName;
    private Boolean isPrivate;
    private List<Participant> participants;

    @Getter
    @Setter
    public static class Participant {

        private Long participantId;
        private Long memberId;
        private String memberNickName;
        private Boolean invitable;

        public Participant(Long participantId, Long memberId, String memberNickName, Boolean invitable) {
            this.participantId = participantId;
            this.memberId = memberId;
            this.memberNickName = memberNickName;
            this.invitable = invitable;
        }
    }

    public ChatRoomDetailInfo(
            Room room,
            List<Participant> participantMap
    ) {
        this.chatRoomId = room.getId();
        this.chatRoomName = room.getRoomName();
        this.isPrivate = room.getIsPrivate();
        this.participants = participantMap;
    }

}
