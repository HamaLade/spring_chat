package com.hs.presentation;

import com.hs.presentation.error.Errors;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ResponseMessage {

    private final String message;
    private final Object data;
    private final ErrorInfo error;

    public ResponseMessage(String message, Object data, ErrorInfo error) {
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public ResponseMessage(String message, Object data) {
        this(message, data, null);
    }

    public ResponseMessage(String message, ErrorInfo error) {
        this(message, null, error);
    }

    public ResponseMessage(String message) {
        this(message, null, null);
    }

    @Getter
    public static class ErrorInfo {
        private final String message;
        private final int code;
        private final int status;
        private final String description;

        public ErrorInfo(String message, int code, int status, String description) {
            this.message = message;
            this.code = code;
            this.status = status;
            this.description = description;
        }
    }

    public static ResponseEntity<ResponseMessage> errorResponseEntity(Errors error, String message) {
        return ResponseEntity.status(error.getStatus()).body(new ResponseMessage(
                        "error"
                        , new ErrorInfo(
                                error.getDefaultErrorMessage()
                                , error.getCode()
                                , error.getStatus()
                                , message
                        )
                )
        );
    }

}
