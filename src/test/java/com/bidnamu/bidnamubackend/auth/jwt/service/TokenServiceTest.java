package com.bidnamu.bidnamubackend.auth.jwt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.bidnamu.bidnamubackend.auth.config.TokenProvider;
import com.bidnamu.bidnamubackend.auth.domain.RefreshToken;
import com.bidnamu.bidnamubackend.auth.exception.UnknownRefreshTokenException;
import com.bidnamu.bidnamubackend.auth.service.RefreshTokenService;
import com.bidnamu.bidnamubackend.auth.service.TokenService;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.service.UserService;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @Mock
  private TokenProvider tokenProvider;

  @Mock
  private RefreshTokenService refreshTokenService;

  @Mock
  private UserService userService;

  @InjectMocks
  private TokenService tokenService;

  @Test
  @DisplayName("유효한 Refresh 토큰이 주어졌을 때, 새로운 Access 토큰을 반환한다.")
  void createNewAccessToken_ValidInput_ReturnAccessToken() {
    // given
    String refreshToken = "valid_refresh_token";
    Long userId = 1L;
    User user = User.builder()
        .email("test@testmail.com")
        .nickname("kimkimkim")
        .password("123456zZxxX!")
        .build();
    String expectedAccessToken = "valid_access_token";

    when(tokenProvider.validToken(refreshToken)).thenReturn(true);
    when(refreshTokenService.findByRefreshToken(refreshToken)).thenReturn(new RefreshToken(userId, refreshToken));
    when(userService.findById(userId)).thenReturn(user);
    when(tokenProvider.generateToken(user, Duration.ofDays(2))).thenReturn(expectedAccessToken);

    // when
    String actualAccessToken = tokenService.createNewAccessToken(refreshToken);

    // then
    assertEquals(expectedAccessToken, actualAccessToken);
  }

  @Test
  @DisplayName("유효하지 않은 Refresh 토큰이 주어졌을 때, UnknownRefreshTokenException을 발생시킨다.")
  void createNewAccessToken_InvalidToken_ThrowException() {
    // given
    String refreshToken = "invalid_refresh_token";

    when(tokenProvider.validToken(refreshToken)).thenReturn(false);

    // then
    assertThrows(UnknownRefreshTokenException.class, () -> {
      // when
      tokenService.createNewAccessToken(refreshToken);
    });
  }
}
