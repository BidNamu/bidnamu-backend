package com.bidnamu.bidnamubackend.user.dto.response;

import com.bidnamu.bidnamubackend.user.domain.User;

public record RegistrationResponseDto(String email, String nickname) {

    public static RegistrationResponseDto from(final User user) {
        return new RegistrationResponseDto(user.getEmail(), user.getNickname());
    }
}
