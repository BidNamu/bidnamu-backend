package com.bidnamu.bidnamubackend.auth.dto.response;

public record LogoutResponseDto(String accessToken, Long expiration) {

}
