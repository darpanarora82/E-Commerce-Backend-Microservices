package com.microservice.productservice.exception;

import com.microservice.productservice.model.ErrorResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<ErrorResponseMessage> handleException(ProductServiceCustomException exception) {
        return new ResponseEntity<>(new ErrorResponseMessage().builder()
                .errorMessage(exception.getMessage())
                .ErrorCode(exception.getErrorCode())
                .build(), HttpStatus.NOT_FOUND);
    }
}
