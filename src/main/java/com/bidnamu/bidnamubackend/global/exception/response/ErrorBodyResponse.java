package com.bidnamu.bidnamubackend.global.exception.response;

public record ErrorBodyResponse(
    ErrorResponse errorInfo,
    Object body
) {

}
