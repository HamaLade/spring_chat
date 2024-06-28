package com.hs.persistence.entity.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 회원 Entity
 * 회원 정보를 담당
 */
@Entity
@Getter
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    /**
     * 회원 식별자
     */
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    /**
     * 회원 로그인 아이디
     */
    @Column(unique = true, updatable = false, nullable = false, length = 50, columnDefinition = "VARCHAR")
    private String loginId;

    /**
     * 회원 닉네임
     */
    @Column(unique = true, nullable = false, length = 50, columnDefinition = "VARCHAR")
    private String nickname;

    /**
     * 회원 비밀번호
     */
    @Column(nullable = false, length = 250, columnDefinition = "VARCHAR")
    private String password;

    /**
     * 회원 권한
     * MemberRole Enum 참조
     */
    @Column(length = 30, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    /**
     * 회원 생성일
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Member(String loginId, String nickname, String password) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.password = password;
        this.role = MemberRole.ROLE_USER;
    }
}
