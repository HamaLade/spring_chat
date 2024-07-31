package com.hs.application.member.exception;

import com.hs.application.ApplicationException;

public class PasswordNotMatchedException extends ApplicationException {

        public PasswordNotMatchedException() {
            super("비밀번호가 일치하지 않습니다.");
        }

        public PasswordNotMatchedException(String message) {
            super(message);
        }

        public PasswordNotMatchedException(String message, Throwable cause) {
            super(message, cause);
        }

}
