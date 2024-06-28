package com.hs.persistence.entity.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 회원 권한 Enum
 */
@Getter
@RequiredArgsConstructor
public enum MemberRole {

    // 관리자
    ROLE_ADMIN,
    // 사용자
    ROLE_USER
    ;

}
