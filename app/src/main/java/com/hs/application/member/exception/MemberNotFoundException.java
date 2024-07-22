package com.hs.application.member.exception;

import com.hs.application.ApplicationException;

public class MemberNotFoundException extends ApplicationException {

        public MemberNotFoundException() {
            super("멤버를 찾을 수 없습니다.");
        }

        public MemberNotFoundException(String message) {
            super(message);
        }

        public MemberNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
}
