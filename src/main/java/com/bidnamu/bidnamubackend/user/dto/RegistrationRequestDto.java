package com.bidnamu.bidnamubackend.user.dto;

import com.bidnamu.bidnamubackend.global.annotation.validation.Email;
import com.bidnamu.bidnamubackend.global.annotation.validation.Nickname;
import com.bidnamu.bidnamubackend.global.annotation.validation.Password;
import com.bidnamu.bidnamubackend.user.domain.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class RegistrationRequestDto {
    @Nickname
    private String nickname;
    @Email
    private String email;
    @Password
    private String password;

    public static User toEntity(RegistrationRequestDto registrationForm, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(registrationForm.getEmail())
                .password(passwordEncoder.encode(registrationForm.getPassword()))
                .nickname(registrationForm.getNickname())
                .build();
    }

}
