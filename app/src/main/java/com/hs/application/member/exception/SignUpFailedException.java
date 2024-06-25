package com.hs.application.member.exception;

import com.hs.application.ApplicationException;

public class SignUpFailedException extends ApplicationException {

    public SignUpFailedException() {
        super("회원가입에 실패하였습니다.");
    }

    public SignUpFailedException(String message) {
        super(message);
    }

    public SignUpFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
