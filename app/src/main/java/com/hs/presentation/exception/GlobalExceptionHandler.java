package com.hs.presentation.exception;

import com.hs.application.ApplicationException;
import com.hs.presentation.ResponseMessage;
import com.hs.presentation.error.Errors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ResponseMessage> handleApplicationException(ApplicationException e) {
        log.error(e.getMessage(), e);
        Errors error = Errors.fromException(e);
        return ResponseMessage.errorResponseEntity(
                Objects.requireNonNullElse(error, Errors.UNDEFINED_ERROR)
        );
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        String badRequestErrorInformation = builder.toString();

        log.error(badRequestErrorInformation, e);

        return ResponseMessage.errorResponseEntity(
                Errors.INVALID_INPUT_VALUE
        );

    }

}
