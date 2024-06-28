package com.hs.application.member.exception;

/**
 * 로그인 정보가 일치하지 않을 때 발생하는 예외
 */
public class LoginInfoNotMatched extends LoginFailedException {
    public LoginInfoNotMatched() {
        super("로그인 정보가 일치하지 않습니다.");
    }

    public LoginInfoNotMatched(String message) {
        super(message);
    }

    public LoginInfoNotMatched(String message, Throwable cause) {
        super(message, cause);
    }
}
