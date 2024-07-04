package com.hs.application.board.exception;

import com.hs.application.ApplicationException;

public class BoardPostNotFoundException extends ApplicationException {

        public BoardPostNotFoundException() {
            super("게시글을 찾을 수 없습니다.");
        }

        public BoardPostNotFoundException(String message) {
            super(message);
        }
}
