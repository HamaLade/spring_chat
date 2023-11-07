package com.hs.chat.domain.model.user.admin;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AdminGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminGradeSeq;

    private Boolean isEnable;

    private LocalDateTime startDateTime;

    private LocalDateTime expiredDateTime;

}
