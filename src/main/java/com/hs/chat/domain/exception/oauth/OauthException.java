package com.hs.chat.domain.exception.oauth;

public class OauthException extends RuntimeException {

        public OauthException(String message) {
            super(message);
        }

        public OauthException(String message, Throwable cause) {
            super(message, cause);
        }
}
