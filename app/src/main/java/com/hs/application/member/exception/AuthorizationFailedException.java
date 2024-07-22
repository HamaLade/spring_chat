package com.hs.application.member.exception;

import com.hs.application.ApplicationException;

public class AuthorizationFailedException extends ApplicationException {

        public AuthorizationFailedException() {
            super("인증에 실패하였습니다.");
        }

        public AuthorizationFailedException(String message) {
            super(message);
        }

        public AuthorizationFailedException(String message, Throwable cause) {
            super(message, cause);
        }
}
