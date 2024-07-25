package com.hs.presentation.error;

import com.hs.application.ApplicationException;
import com.hs.application.InvalidInputValueException;
import com.hs.application.TooManyRequestException;
import com.hs.application.UndefindException;
import com.hs.application.board.exception.BoardPostNotFoundException;
import com.hs.application.member.exception.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러 코드 정의 Enum
 * 메세지, 코드, HTTP 상태코드를 가짐
 */
@Getter
@RequiredArgsConstructor
public enum Errors {

    // 올바르지 않은 값에 의한 요청 오류
    INVALID_INPUT_VALUE("잘못된 입력값입니다.", 400, InvalidInputValueException.class),
    TOO_MANY_REQUEST("요청이 너무 많습니다.", 429, TooManyRequestException.class),
    UNDEFINED_ERROR("알 수 없는 오류가 발생했습니다.", 500, UndefindException.class),
    MEMBER_ID_EXISTS("이미 사용중인 아이디입니다.", 400, MemberLoginIdExistsException.class),
    MEMBER_NICKNAME_EXISTS("이미 사용중인 닉네임입니다.", 400, MemberNicknameExistsException.class),
    LOGIN_FAILED("로그인에 실패했습니다.", 400, LoginFailedException.class),
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", 400, MemberNotFoundException.class),
    BOARD_POST_NOT_FOUND("해당하는 게시물을 찾을 수 없습니다.", 400, BoardPostNotFoundException.class);

    private final String defaultErrorMessage;
    private final int status;
    private final Class<? extends ApplicationException> exceptionClass;

    public int getCode() {
        return 1000 + this.ordinal();
    }

    /*
    ApplicationException을 상속받은 예외를 파라미터로 받아서 Errors의 exceptionClass와 같은 클래스를 찾아 해당 Errors를 반환
     */
    public static Errors fromException(ApplicationException e) {
        for (Errors error : values()) {
            if (error.exceptionClass.isInstance(e)) {
                return error;
            }
        }
        return null;
    }
}
