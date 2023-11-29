package com.bidnamu.bidnamubackend.global.exception.handler;

import com.bidnamu.bidnamubackend.global.exception.error_code.CommonErrorCode;
import com.bidnamu.bidnamubackend.global.exception.response.ErrorResponse;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedNicknameException;
import java.util.NoSuchElementException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
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
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedNicknameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedNicknameException(
        final DuplicatedNicknameException e) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException e) {
        final FieldError error = e.getBindingResult().getFieldErrors().get(0);
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, error.getDefaultMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(
        final NoSuchElementException e) {
        final var errorResponse = createErrorResponse(RESOURCE_NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(
        final FileUploadException e) {
        final var errorResponse = createErrorResponse(BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ErrorResponse createErrorResponse(final CommonErrorCode errorCode,
        final String explain) {
        return ErrorResponse.builder().code(errorCode).message(errorCode.getMessage())
            .explain(explain).build();
    }
}
