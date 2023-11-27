package com.bidnamu.bidnamubackend.auth.domain;

import com.bidnamu.bidnamubackend.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken extends BaseEntity {

  @Column(name = "user_id", nullable = false, unique = true)
  private Long userId;

  @Column(name = "refresh_token_value", nullable = false)
  private String refreshTokenValue;

  public RefreshToken(Long userId, String refreshToken) {
    this.userId = userId;
    this.refreshTokenValue = refreshToken;
  }

  public RefreshToken update(String newRefreshToken) {
    this.refreshTokenValue = newRefreshToken;
    return this;
  }
}
