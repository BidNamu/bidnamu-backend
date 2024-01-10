package com.bidnamu.bidnamubackend.auth.dto.request;

public record LogoutRequestDto(String accessToken, String refreshToken) {

}
