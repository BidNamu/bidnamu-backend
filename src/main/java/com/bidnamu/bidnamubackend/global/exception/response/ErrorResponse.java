package com.bidnamu.bidnamubackend.global.exception.response;

import com.bidnamu.bidnamubackend.global.exception.error_code.ErrorCode;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorResponse(ErrorCode code, String message, String explain) {

    public ResponseEntity<ErrorResponse> toResponseEntity() {
        return ResponseEntity.status(code.getHttpStatus()).body(this);
    }
}
