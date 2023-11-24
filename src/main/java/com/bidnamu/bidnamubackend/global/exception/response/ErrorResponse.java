package com.bidnamu.bidnamubackend.global.exception.response;

import com.bidnamu.bidnamubackend.global.exception.error_code.ErrorCode;
import lombok.Builder;

@Builder
public record ErrorResponse(ErrorCode code, String message, String explain) {
}
