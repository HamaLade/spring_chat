package com.hs.persistence.entity.file;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Table
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long fileSize;

    @Column(length = 10, columnDefinition = "VARCHAR")
    private String fileNameExtension;

    @Column(nullable = false, length = 512)
    private String fileName;

    @Column(nullable = false, length = 512)
    private String fileNameOriginal;

    // 파일을 참조하는 테이블의 ID
    private Long referrerId;

    @Enumerated(EnumType.STRING)
    private FileReferrer fileReferrer;

    @CreatedDate
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

}
