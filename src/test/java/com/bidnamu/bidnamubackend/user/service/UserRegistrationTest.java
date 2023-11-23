package com.bidnamu.bidnamubackend.user.service;

import com.bidnamu.bidnamubackend.user.domain.User;
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
    UserService userService;

    @Resource
    UserRepository userRepository;


    @Test
    @Transactional
    @DisplayName("사용자가 회원가입을 요청하였을 경우 요청한 정보대로 DB에 저장된다")
    void successToRegistration() {
        // Given
        User user = User.builder().email("rudals1888@gmail.com").nickname("kimkim")
            .password("fefefass1Z!z").build();

        // When
        userService.registration(user);

        // Then
        User foundUser = userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        assertEquals(foundUser.getEmail(), user.getEmail());
    }

    @Test
    @Transactional
    @DisplayName("사용자가 이미 존재하는 Email로 회원가입을 요청하였을 경우 DuplicatedEmailException을 발생시킨다")
    void failToRegistrationDuplicatedEmail() {
        // Given
        String email = "rudals1888@gmail.com";
        // 이미 존재하는 사용자
        userRepository.save(
            User.builder()
                .email(email)
                .nickname("kimkim")
                .password("fefefass1Z!z")
                .build());

        User newUser = User.builder()
            .email(email)
            .nickname("kimkim1")
            .password("fefefass1Z!z!")
            .build();

        // Then
        assertThrows(DuplicatedEmailException.class, () -> userService.registration(newUser));
    }
}