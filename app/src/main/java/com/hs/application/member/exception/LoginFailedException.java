package com.hs.application.member.exception;

import com.hs.application.ApplicationException;

public class LoginFailedException extends ApplicationException {

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
