package com.bidnamu.bidnamubackend.auth.service;

import com.bidnamu.bidnamubackend.auth.domain.RefreshToken;
import com.bidnamu.bidnamubackend.auth.exception.UnknownRefreshTokenException;
import com.bidnamu.bidnamubackend.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshToken findByRefreshToken(final String refreshToken) {
    return refreshTokenRepository.findByRefreshTokenValue(refreshToken)
        .orElseThrow(() -> new UnknownRefreshTokenException("유효하지 않은 토큰입니다."));
  }

}
