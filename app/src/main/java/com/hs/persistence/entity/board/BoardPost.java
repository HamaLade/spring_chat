package com.hs.persistence.entity.board;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 게시판 게시글 Entity
 */
@Getter
@Entity
@Table(name = "board_post")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardPost {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    // 게시판 게시글 제목
    @Column(nullable = false, length = 255, columnDefinition = "VARCHAR")
    private String title;

    // 게시판 게시글 내용
    @Column(length = 5000, columnDefinition = "TEXT")
    private String textContent;

    // 게시판 게시글 파일 첨부 여부
    private boolean hasFile;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

}
