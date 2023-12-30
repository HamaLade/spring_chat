package com.hs.chat.domain.model.chat.message;

import com.hs.chat.domain.enums.chat.ChatType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageSeq;

    private Long chatRoomSeq;

    private Long userSeq;

    private String message;

    private ChatType chatType;

    @CreatedDate
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;

    public void enter(String name) {
            this.message = name + "님이 입장하였습니다.";
    }

    @Builder
    public ChatMessage(Long chatRoomSeq, Long userSeq, String message, ChatType chatType, LocalDateTime createdDateTime, LocalDateTime updateDateTime) {
        this.chatRoomSeq = chatRoomSeq;
        this.userSeq = userSeq;
        this.message = message;
        this.chatType = chatType;
    }
}
