package com.bidnamu.bidnamubackend.global.exception.handler;

import com.bidnamu.bidnamubackend.global.exception.error_code.CommonErrorCode;
import com.bidnamu.bidnamubackend.global.exception.response.ErrorResponse;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicationEmailException(DuplicatedEmailException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code(CommonErrorCode.INVALID_PARAMETER)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldErrors().get(0);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code(CommonErrorCode.INVALID_PARAMETER)
                .message(error.getDefaultMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
