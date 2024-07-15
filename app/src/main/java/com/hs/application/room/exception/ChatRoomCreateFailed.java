package com.hs.application.room.exception;

import com.hs.application.ApplicationException;

public class ChatRoomCreateFailed extends ApplicationException {

        public ChatRoomCreateFailed(String message) {
            super(message);
        }

        public ChatRoomCreateFailed(String message, Throwable cause) {
            super(message, cause);
        }
}
