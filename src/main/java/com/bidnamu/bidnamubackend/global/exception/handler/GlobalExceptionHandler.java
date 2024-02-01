package com.bidnamu.bidnamubackend.global.exception.handler;

import com.bidnamu.bidnamubackend.auth.exception.UnknownTokenException;
import com.bidnamu.bidnamubackend.bid.exception.BidException;
import com.bidnamu.bidnamubackend.bid.exception.NotEnoughCreditException;
import com.bidnamu.bidnamubackend.file.exception.FileUploadException;
import com.bidnamu.bidnamubackend.global.exception.error_code.CommonErrorCode;
import com.bidnamu.bidnamubackend.global.exception.response.ErrorBodyResponse;
import com.bidnamu.bidnamubackend.global.exception.response.ErrorResponse;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedNicknameException;
import com.bidnamu.bidnamubackend.user.exception.UnknownUserException;
import com.siot.IamportRestClient.exception.IamportResponseException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.security.SignatureException;
import java.util.NoSuchElementException;
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

    @ExceptionHandler(UnknownUserException.class)
    public ResponseEntity<ErrorResponse> handleUnknownUserException(
        final UnknownUserException e
    ) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
    }

    @ExceptionHandler(UnknownTokenException.class)
    public ResponseEntity<ErrorResponse> handleUnknownRefreshTokenException(
        final UnknownTokenException e
    ) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(final SignatureException e) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(
        final MalformedJwtException e) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(final ExpiredJwtException e) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedJwtException(
        final UnsupportedJwtException e) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(final JwtException e) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException e) {
        final FieldError error = e.getBindingResult().getFieldErrors().get(0);
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, error.getDefaultMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(
        final FileUploadException e) {
        final var errorResponse = createErrorResponse(BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(
        final NoSuchElementException e) {
        final var errorResponse = createErrorResponse(RESOURCE_NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IamportResponseException.class)
    public ResponseEntity<ErrorResponse> handleIamportException(
        final IamportResponseException e
    ) {
        return switch (e.getHttpStatusCode()) {
            case 401 -> createErrorResponse(UNAUTHORIZED, "401 Unauthorized").toResponseEntity();
            case 404 ->
                createErrorResponse(RESOURCE_NOT_FOUND, "해당하는 거래내역을 찾을 수 없습니다.").toResponseEntity();
            default -> createErrorResponse(INTERNAL_SERVER_ERROR, "서버 응답 오류").toResponseEntity();
        };
    }

    @ExceptionHandler(NotEnoughCreditException.class)
    public ResponseEntity<ErrorBodyResponse> handleNotEnoughCreditException(
        final NotEnoughCreditException e
    ) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        final var errorBodyResponse = new ErrorBodyResponse(errorResponse, e.getResponseDto());
        return ResponseEntity.status(errorResponse.code().getHttpStatus()).body(errorBodyResponse);
    }

    @ExceptionHandler(BidException.class)
    public ResponseEntity<ErrorResponse> handleBidException(
        final BidException e
    ) {
        final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ErrorResponse createErrorResponse(final CommonErrorCode errorCode,
        final String explain) {
        return ErrorResponse.of(errorCode, explain);
    }
}
