package com.hs.error;

import com.hs.presentation.error.Errors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/errors")
public class ErrorCodeController {

    @ResponseBody
    @GetMapping
    public ResponseEntity<ErrorCodeView> getErrorCodes(){

        Map<String, Object> errorCodes = Arrays.stream(Errors.values())
                .collect(Collectors.toMap(error -> String.valueOf(error.getCode()), Errors::getDescription));

        return new ResponseEntity<>(new ErrorCodeView(errorCodes), HttpStatus.OK);
    }

}
