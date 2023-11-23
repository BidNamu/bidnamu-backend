package com.bidnamu.bidnamubackend.global.exception.handler;

import com.bidnamu.bidnamubackend.global.exception.response.ErrorResponse;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.bidnamu.bidnamubackend.global.exception.error_code.CommonErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicationEmailException(
        final DuplicatedEmailException e) {
        final var errorCode = INVALID_PARAMETER;
        final var errorResponse = ErrorResponse.builder()
            .code(errorCode)
            .message(errorCode.getMessage())
            .explain(e.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException e) {
        final var errorCode = INVALID_PARAMETER;
        final FieldError error = e.getBindingResult().getFieldErrors().get(0);
        final var errorResponse = ErrorResponse.builder()
            .code(errorCode)
            .message(errorCode.getMessage())
            .explain(error.getDefaultMessage())
            .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
