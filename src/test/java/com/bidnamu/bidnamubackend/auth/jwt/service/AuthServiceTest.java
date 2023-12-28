package com.bidnamu.bidnamubackend.auth.jwt.service;

import com.bidnamu.bidnamubackend.auth.dto.request.LoginRequestDto;
import com.bidnamu.bidnamubackend.auth.dto.response.LoginResponseDto;
import com.bidnamu.bidnamubackend.auth.service.AuthService;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.dto.request.RegistrationRequestDto;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import com.bidnamu.bidnamubackend.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class AuthServiceTest {

  @Autowired
  private AuthService authService;
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;

  private final String nickname = "myNameIs";
  private final String email = "test1234@gmail.com";
  private final String password = "1234123zXcC!";


  @BeforeEach
  void setting() {
    final var user = new RegistrationRequestDto(nickname,email,password);
    userService.createUser(user);

  }

  @Test
  @Transactional
  @DisplayName("유저가 로그인할 때 액세스토큰과 리프레쉬토큰을 지급받는다.")
  void login() {

    final LoginRequestDto loginRequestDto = new LoginRequestDto(email,password);
    final User user = userRepository.findByEmail(email).orElseThrow();

    final LoginResponseDto loginResponseDto = authService.processLogin(loginRequestDto);
    Assertions.assertNotNull(loginResponseDto.accessToken());
    Assertions.assertNotNull(user.getRefreshToken());

  }

  @Test
  @Transactional
  @DisplayName("액세스 토큰을 갱신한다.")
  void RefreshToken() {
    final LoginRequestDto loginRequestDto = new LoginRequestDto(email,password);

    final LoginResponseDto responseDto = authService.processLogin(loginRequestDto);

    Assertions.assertNotNull(authService.refreshToken(responseDto.refreshToken()));


  }

}
