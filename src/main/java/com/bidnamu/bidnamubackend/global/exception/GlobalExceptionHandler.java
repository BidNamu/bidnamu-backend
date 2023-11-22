package com.bidnamu.bidnamubackend.global.exception;

import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<Object> handleDuplicationEmailException(DuplicatedEmailException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("Duplicated Email !")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

}
