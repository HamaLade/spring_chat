package com.hs.presentation.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러 코드 정의 Enum
 * 메세지, 코드, HTTP 상태코드를 가짐
 */
@Getter
@RequiredArgsConstructor
public enum Errors {

    // 회원가입 아이디 미입력
    EMPTY_ID("아이디를 입력해주세요.", 1000, 400),

    // 회원가입 아이디 중복
    DUPLICATED_ID("이미 사용중인 아이디입니다.", 1001, 400),

    // 회원가입 닉네임 중복
    DUPLICATED_NICKNAME("이미 사용중인 닉네임입니다.", 1002, 400),

    // 회원가입 비밀번호 미입력
    EMPTY_PASSWORD("비밀번호를 입력해주세요.", 1003, 400),

    // 로그인 실패
    LOGIN_FAILED("로그인에 실패했습니다.", 1004, 400)

    ;

    private final String message;
    private final int code;
    private final int status;

}
