package com.hs.application;

public class InvalidInputValueException extends ApplicationException {

    public InvalidInputValueException() {
        super("유효하지 않은 입력값입니다.");
    }

    public InvalidInputValueException(String message) {
        super(message);
    }

    public InvalidInputValueException(String message, Throwable cause) {
        super(message, cause);
    }

}
