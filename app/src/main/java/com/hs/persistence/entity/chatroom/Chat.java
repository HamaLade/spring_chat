package com.hs.persistence.entity.chatroom;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "chat")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, columnDefinition = "VARCHAR", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @Column(length = 500, columnDefinition = "VARCHAR")
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    private String senderNickname;

    private Boolean hasFiles;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createDate;

    public Chat(ChatType chatType, String message, Room room, String senderNickname, Boolean hasFiles) {
        this.chatType = chatType;
        this.message = message;
        this.room = room;
        this.senderNickname = senderNickname;
        this.hasFiles = hasFiles != null && hasFiles;
    }
}
