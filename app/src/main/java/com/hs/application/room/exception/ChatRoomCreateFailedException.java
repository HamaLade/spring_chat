package com.hs.application.room.exception;

import com.hs.application.ApplicationException;

public class ChatRoomCreateFailedException extends ApplicationException {

        public ChatRoomCreateFailedException(String message) {
            super(message);
        }

        public ChatRoomCreateFailedException(String message, Throwable cause) {
            super(message, cause);
        }
}
