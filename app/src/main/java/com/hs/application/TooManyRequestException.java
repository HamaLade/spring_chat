package com.hs.application;

public class TooManyRequestException extends ApplicationException {

        public TooManyRequestException() {
            super("요청 횟수가 너무 많습니다.");
        }

        public TooManyRequestException(String message) {
            super(message);
        }

        public TooManyRequestException(String message, Throwable cause) {
            super(message, cause);
        }
}
