package com.hs.application.member.exception;

import com.hs.application.ApplicationException;

/**
 * 회원 닉네임 중복 예외
 */
public class MemberNicknameExistsException extends ApplicationException {

    public MemberNicknameExistsException() {
        super("닉네임이 이미 존재합니다.");
    }

    public MemberNicknameExistsException(String message) {
        super(message);
    }

    public MemberNicknameExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
