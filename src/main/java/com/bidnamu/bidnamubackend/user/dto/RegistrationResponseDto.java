package com.bidnamu.bidnamubackend.user.dto;

import com.bidnamu.bidnamubackend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegistrationResponseDto {
    private String email;
    private String nickname;

    public RegistrationResponseDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
