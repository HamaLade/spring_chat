package com.hs.chat.domain.model.chat.message;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatMessageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageFileSeq;

    private Long chatMessageSeq;

    private Long fileSeq;

    private Boolean isDelete;

    @CreatedDate
    private LocalDateTime createdDateTime;

}
