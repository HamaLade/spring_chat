package com.hs.chat.domain.exception.oauth;

public class UnsupportedRegistration extends OauthException {


    public UnsupportedRegistration(String message) {
        super(message);
    }

    public UnsupportedRegistration(String message, Throwable cause) {
        super(message, cause);
    }
}
