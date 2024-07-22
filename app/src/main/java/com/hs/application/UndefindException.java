package com.hs.application;

public class UndefindException extends ApplicationException {
    public UndefindException() {
        super("정의되지 않은 에러입니다.");
    }

    public UndefindException(String message) {
        super(message);
    }

    public UndefindException(String message, Throwable cause) {
        super(message, cause);
    }
}
