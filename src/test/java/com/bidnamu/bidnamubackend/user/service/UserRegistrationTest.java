package com.bidnamu.bidnamubackend.user.service;

import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.dto.RegistrationRequestDto;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRegistrationTest {

    @Resource
    private UserService userService;

    @Resource
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("사용자가 회원가입을 요청하였을 경우 요청한 정보대로 DB에 저장된다")
    void successToRegistration() {
        // Given
        RegistrationRequestDto requestDto = new RegistrationRequestDto(
            "rudals1888@gmail.com", "kimkim", "fefefass1Z!z");

        // When
        userService.createUser(requestDto);

        // Then
        User foundUser = userRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        assertEquals(foundUser.getEmail(), requestDto.email());
    }

    @Test
    @Transactional
    @DisplayName("사용자가 이미 존재하는 Email로 회원가입을 요청하였을 경우 DuplicatedEmailException을 발생시킨다")
    void failToRegistrationDuplicatedEmail() {
        // Given
        String email = "rudals1888@gmail.com";

        // When
        userRepository.save(
            User.builder()
                .email(email)
                .nickname("kimkim")
                .password("fefefass1Z!z")
                .build());

        // Then
        RegistrationRequestDto requestDto = new RegistrationRequestDto(
            "kimkim2", email, "fefefass1Z!z");
        assertThrows(DuplicatedEmailException.class, () -> userService.createUser(requestDto));
    }
}