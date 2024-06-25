package com.hs.application.member.exception;

import com.hs.application.ApplicationException;

public class AuthorizationFailed extends ApplicationException {

        public AuthorizationFailed() {
            super("인증에 실패하였습니다.");
        }

        public AuthorizationFailed(String message) {
            super(message);
        }

        public AuthorizationFailed(String message, Throwable cause) {
            super(message, cause);
        }
}
