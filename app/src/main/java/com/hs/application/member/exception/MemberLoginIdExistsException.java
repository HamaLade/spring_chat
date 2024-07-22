package com.hs.application.member.exception;


import com.hs.application.ApplicationException;

public class MemberLoginIdExistsException extends ApplicationException {

        public MemberLoginIdExistsException() {
            super("로그인 아이디가 이미 존재합니다.");
        }

        public MemberLoginIdExistsException(String message) {
            super(message);
        }

        public MemberLoginIdExistsException(String message, Throwable cause) {
            super(message, cause);
        }

}
