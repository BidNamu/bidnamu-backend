package com.bidnamu.bidnamubackend.user.service;

import com.bidnamu.bidnamubackend.auth.domain.Role;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.dto.request.UserStatusUpdateRequestDto;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserExpireTest {

    @Resource
    private UserService userService;
    @Resource
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("사용자가 회원탈퇴를 요청하였을 경우 사용자의 상태가 변경된다.")
    void expireUser() {
        // Given
        final User user = userRepository.save(User.builder()
            .email("hw")
            .nickname("hw")
            .password("hw12")
            .build());

        final UserStatusUpdateRequestDto dto = new UserStatusUpdateRequestDto(null, true);

        // When
        userService.updateUserStatus(user.getEmail(), dto);

        // Then
        final User expiredUser = userService.findByEmail(user.getEmail());
        assertTrue(expiredUser.isExpired());
    }

    @Test
    @Transactional
    @DisplayName("관리자가 회원 상태 변경 요청을 하였을 경우 사용자의 상태가 변경된다.")
    void UserAuthentication() {
        // Given
        final User user = userRepository.save(User.builder() // 관리자 계정
            .email("hw")
            .nickname("hw")
            .password("hw12")
            .build());
        user.addAuthority(Role.ROLE_USER);
        user.addAuthority(Role.ROLE_ADMIN);

        final User user2 = userRepository.save(User.builder() // 회원 계정
            .email("km")
            .nickname("km")
            .password("km12")
            .build());
        user2.addAuthority(Role.ROLE_USER);

        final UserStatusUpdateRequestDto dto = new UserStatusUpdateRequestDto(false, true);

        // When
        userService.updateUserStatus(user2.getEmail(), dto);

        // Then
        final User expiredUser = userService.findByEmail(user2.getEmail());
        assertTrue(expiredUser.isExpired());
    }
}