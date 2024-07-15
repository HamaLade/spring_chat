package com.hs.settings.utils;

public class WebSocketAttribute {

    public static final String SESSION_ATTRIBUTE_NAME = "sessionAttr";

    private final String sessionId;
    private Long memberId;

    public WebSocketAttribute(String sessionId) {
        this.sessionId = sessionId;
        this.memberId = null;
    }

}
