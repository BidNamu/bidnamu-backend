package com.bidnamu.bidnamubackend.user.dto.request;

public record UserStatusUpdateRequestDto(
    // 각자 null 시 변경 하지 않음
    Boolean enabled,
    Boolean expired
) {

    public static UserStatusUpdateRequestDto expireRequestDto() {
        return new UserStatusUpdateRequestDto(null, true);
    }
}
