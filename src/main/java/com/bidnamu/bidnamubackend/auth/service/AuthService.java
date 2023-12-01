package com.bidnamu.bidnamubackend.auth.service;

import com.bidnamu.bidnamubackend.auth.config.TokenProvider;
import com.bidnamu.bidnamubackend.auth.dto.request.LoginRequestDto;
import com.bidnamu.bidnamubackend.auth.dto.response.LoginResponseDto;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final TokenProvider tokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final UserService userService;

  @Transactional
  public LoginResponseDto processLogin(final LoginRequestDto requestDto) {
    final Authentication authentication = createAuthentication(requestDto);

    final String accessToken = tokenProvider.generateAccessToken(authentication);
    final String refreshToken = tokenProvider.generateRefreshToken();

    final User user = userService.findByEmail(authentication.getName());

    user.updateRefreshToken(refreshToken);

    return new LoginResponseDto(accessToken, refreshToken);
  }

  @Transactional
  public LoginResponseDto refreshToken(final String refreshToken) {
    tokenProvider.validToken(refreshToken);
    final var user = userService.findByRefreshToken(refreshToken);
    final Authentication authentication = tokenProvider.getAuthentication(user.getRefreshToken());
    final String accessToken = tokenProvider.generateAccessToken(authentication);
    final String generatedRefreshToken = tokenProvider.generateRefreshToken();

    user.updateRefreshToken(refreshToken);

    return new LoginResponseDto(accessToken, generatedRefreshToken);
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
