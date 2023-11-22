package com.bidnamu.bidnamubackend.user.service;

import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserServiceTest {

    @Resource
    UserService userService;

    @Resource
    UserRepository userRepository;


    @Test
    @Transactional
    void registration() {
        // Given
        User user = User.builder().email("rudals1888@gmail.com").nickname("kimkim").password("fefefass1Z!z").build();

        // When
        userService.registration(user);

        // Then
        User foundUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Assertions.assertEquals(foundUser.getEmail(), user.getEmail());
    }
}