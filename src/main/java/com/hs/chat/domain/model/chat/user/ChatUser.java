package com.hs.chat.domain.model.chat.user;

import com.hs.chat.global.enums.UserType;
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
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatUserSeq;

    private Long userSeq; // 유저 시퀀스

    private Long lastReadChatMessageSeq; // 마지막으로 읽은 채팅 메시지 시퀀스

    @Enumerated(EnumType.STRING)
    private UserType userType; // 유저 타입

    private Boolean isLogin; // 로그인 여부

    private Boolean isOut; // 탈퇴 여부

    private Boolean isBlock; // 차단 여부

    @CreatedDate
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;

}
