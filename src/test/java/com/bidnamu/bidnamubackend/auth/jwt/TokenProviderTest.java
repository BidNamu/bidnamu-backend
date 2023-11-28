package com.bidnamu.bidnamubackend.auth.jwt;

import com.bidnamu.bidnamubackend.auth.config.JwtProperties;
import com.bidnamu.bidnamubackend.auth.config.TokenProvider;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
class TokenProviderTest {

  @Autowired
  private TokenProvider tokenProvider;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JwtProperties jwtProperties;

  @DisplayName("generateToken() : 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
  @Test
  void generateToken() {
    //given
    User testUser = userRepository.save(User.builder()
        .email("test@naver.com")
        .nickname("hellosss")
        .password("2ek21eEE!").build());

    //when
    String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

    //then
    Long userId = Jwts.parser()
        .setSigningKey(jwtProperties.getSecretKey())
        .parseClaimsJws(token)
        .getBody()
        .get("id", Long.class);

    Assertions.assertEquals(userId, testUser.getId());
  }

  @DisplayName("validToken(): 만료된 토큰인 대에 유효성 검증에 실패한다.")
  @Test
  void validToken_invalidToken() {
    //given
    String token = JwtFactory.builder()
        .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis())).build()
        .createToken(jwtProperties);

    //when
    boolean result = tokenProvider.validToken(token);

    //then
    Assertions.assertFalse(result);
  }

  @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
  @Test
  void getAuthentication() {
    //given
    String userEmail = "user@email.com";
    String token = JwtFactory.builder().subject(userEmail).build().createToken(jwtProperties);

    //when
    Authentication authentication = tokenProvider.getAuthentication(token);

    //then
    Assertions.assertEquals(((UserDetails) authentication.getPrincipal()).getUsername(), userEmail);
  }

  @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
  @Test
  void getUserId() {
    //given
    Long userId = 1L;
    String token = JwtFactory.builder().claims(Map.of("id", userId)).build()
        .createToken(jwtProperties);

    //when
    Long userIdByToken = tokenProvider.getUserId(token);

    //then
    Assertions.assertEquals(userIdByToken, userId);
  }
}