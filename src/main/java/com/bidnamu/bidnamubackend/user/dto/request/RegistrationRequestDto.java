package com.bidnamu.bidnamubackend.user.dto.request;

import com.bidnamu.bidnamubackend.user.domain.User;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegistrationRequestDto(
        @NotBlank(message = "공백 닉네임은 허용되지 않습니다.")
        @Size(min = 5, message = "최소 5자 이상 입력해주세요")
        String nickname,
        @Email(message = "유효하지 않은 이메일 형식입니다.")
        String email,
        @Pattern(message = "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
                regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!~$%^=()])(?=\\S+$).{8,16}$")
        String password) {

    public User toEntity(final PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .nickname(this.nickname)
                .build();
    }

}
