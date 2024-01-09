package com.hs.chat.domain.model.chat.room;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomSeq;

    private String roomName;

    private String roomPassword;

    private Boolean isDeleted;

    private Boolean isBlocked;

    private Boolean isPublic;

    @CreatedDate
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;

    public static ChatRoom create(String roomName, String roomPassword, Boolean isPublic) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomName = roomName;
        chatRoom.roomPassword = roomPassword == null || roomPassword.isEmpty() ? null : roomPassword;
        chatRoom.isPublic = isPublic;
        return chatRoom;
    }

}
