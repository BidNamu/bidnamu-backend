package com.bidnamu.bidnamubackend.global.exception.handler;

import com.bidnamu.bidnamubackend.auth.exception.UnknownTokenException;
import com.bidnamu.bidnamubackend.global.exception.error_code.CommonErrorCode;
import com.bidnamu.bidnamubackend.global.exception.response.ErrorResponse;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedNicknameException;
import com.bidnamu.bidnamubackend.user.exception.UnknownUserException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.security.SignatureException;
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
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
  }

  @ExceptionHandler(SignatureException.class)
  public ResponseEntity<ErrorResponse> handleSignatureException(final SignatureException e) {
    final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(MalformedJwtException.class)
  public ResponseEntity<ErrorResponse> handleMalformedJwtException(final MalformedJwtException e) {
    final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ErrorResponse> handleExpiredJwtException(final ExpiredJwtException e) {
    final var errorResponse = createErrorResponse(INVALID_PARAMETER, e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException e) {
    final FieldError error = e.getBindingResult().getFieldErrors().get(0);
    final var errorResponse = createErrorResponse(INVALID_PARAMETER, error.getDefaultMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  private ErrorResponse createErrorResponse(final CommonErrorCode errorCode,
      final String explain) {
    return ErrorResponse.builder().code(errorCode).message(errorCode.getMessage())
        .explain(explain).build();
  }

}
