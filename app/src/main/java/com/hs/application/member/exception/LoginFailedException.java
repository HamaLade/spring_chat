package com.hs.application.member.exception;

public class LoginFailedException extends RuntimeException {

    public LoginFailedException() {
        super("로그인에 실패하였습니다.");
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
