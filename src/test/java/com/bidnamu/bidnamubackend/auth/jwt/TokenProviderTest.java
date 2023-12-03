package com.bidnamu.bidnamubackend.auth.jwt;

import com.bidnamu.bidnamubackend.auth.config.JwtProperties;
import com.bidnamu.bidnamubackend.auth.config.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {


  private TokenProvider tokenProvider;
  private JwtProperties jwtProperties;

  @Autowired
  public TokenProviderTest(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.tokenProvider = new TokenProvider(jwtProperties);
  }


  @Test
  @Transactional
  @DisplayName("Jwtproperties 바인딩 테스트")
  void jwtPropertiesBindingTest() {
    assertEquals("kimmin1", jwtProperties.getIssuer());
  }

  @Test
  @Transactional
  @DisplayName("리프레쉬 토큰을 생성한다.")
  void generateRefreshTokenTest() {
    String token = tokenProvider.generateRefreshToken();
    assertNotNull(token);
  }


}
