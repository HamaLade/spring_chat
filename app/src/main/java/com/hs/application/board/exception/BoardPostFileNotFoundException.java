package com.hs.application.board.exception;

import com.hs.application.ApplicationException;

public class BoardPostFileNotFoundException extends ApplicationException {

            public BoardPostFileNotFoundException() {
                super("게시글 파일을 찾을 수 없습니다.");
            }

            public BoardPostFileNotFoundException(String message) {
                super(message);
            }
}
