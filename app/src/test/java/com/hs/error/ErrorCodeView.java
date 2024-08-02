package com.hs.error;

import java.util.Map;

public class ErrorCodeView {
    private Map<String, Object> errorCodes;

    public ErrorCodeView(Map<String, Object> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public Map<String, Object> getErrorCodes() {
        return errorCodes;
    }

    public ErrorCodeView() {
    }
}
