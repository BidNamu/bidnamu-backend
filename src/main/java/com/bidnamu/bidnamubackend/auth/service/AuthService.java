package com.bidnamu.bidnamubackend.auth.service;

import com.bidnamu.bidnamubackend.auth.config.TokenProvider;
import com.bidnamu.bidnamubackend.auth.dto.request.LoginRequestDto;
import com.bidnamu.bidnamubackend.auth.dto.response.LoginResponseDto;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.exception.UnknownUserException;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

  private final TokenProvider tokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final UserRepository userRepository;

  @Transactional
  public LoginResponseDto processLogin(final LoginRequestDto requestDto) {
    final Authentication authentication = createAuthentication(requestDto);

    final String accessToken = tokenProvider.generateAccessToken(authentication);
    final String refreshToken = tokenProvider.generateRefreshToken();

    final User user = userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new UnknownUserException("유저를 찾을 수 없습니다."));

    user.updateRefreshToken(refreshToken);

    return new LoginResponseDto(accessToken, refreshToken);
  }

  private Authentication createAuthentication(final LoginRequestDto loginRequestDto) {
    final Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
        loginRequestDto.email(), loginRequestDto.password());
    final var authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return authentication;
  }


}
