package com.bidnamu.bidnamubackend.user.dto.response;

import com.bidnamu.bidnamubackend.user.domain.User;

public record UserStatusUpdateResponseDto(Boolean enabled, Boolean expired) {

    public static UserStatusUpdateResponseDto from(final User user) {
        return new UserStatusUpdateResponseDto(user.isEnabled(), user.isExpired());
    }
}
