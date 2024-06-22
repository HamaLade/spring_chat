package com.hs.persistance.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 Entity
 * 회원 정보를 담당
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    /**
     * 회원 식별자
     */
    @Id
    private Long id;

    /**
     * 회원 로그인 아이디
     */
    @Column(unique = true, updatable = false, nullable = false, length = 50, columnDefinition = "VARCHAR")
    private String loginId;

    /**
     * 회원 닉네임
     */
    @Column(nullable = false, length = 50, columnDefinition = "VARCHAR")
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
    private MemberRole role;

}
