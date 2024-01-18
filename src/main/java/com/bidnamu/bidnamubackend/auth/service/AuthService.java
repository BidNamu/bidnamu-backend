package com.bidnamu.bidnamubackend.auth.service;

import com.bidnamu.bidnamubackend.auth.config.JwtProperties;
import com.bidnamu.bidnamubackend.auth.config.TokenProvider;
import com.bidnamu.bidnamubackend.auth.domain.RefreshToken;
import com.bidnamu.bidnamubackend.auth.dto.request.LoginRequestDto;
import com.bidnamu.bidnamubackend.auth.dto.response.LoginResponseDto;
import com.bidnamu.bidnamubackend.auth.dto.response.LogoutResponseDto;
import com.bidnamu.bidnamubackend.auth.exception.UnknownTokenException;
import com.bidnamu.bidnamubackend.auth.repository.RefreshTokenRedisRepository;
import com.bidnamu.bidnamubackend.user.service.UserService;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;


    @Transactional
    public LoginResponseDto processLogin(final LoginRequestDto requestDto) {
        final Authentication authentication = createAuthentication(requestDto);

        final String accessToken = tokenProvider.generateAccessToken(authentication);
        final String refreshToken = tokenProvider.generateRefreshToken();

        refreshTokenRedisRepository.save(
            RefreshToken.builder().username(authentication.getName()).token(refreshToken)
                .expiration(jwtProperties.getRefreshTokenExpiration() / 1000).build());

        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public LoginResponseDto refreshToken(final String refreshToken) {

        if (!tokenProvider.validToken(refreshToken)) {
            throw new UnknownTokenException("잘못되거나 만료된 토큰입니다.");
        }

        final var token = refreshTokenRedisRepository.findByToken(refreshToken);
        final var user = userService.findByEmail(token.getUsername());
        final Collection<? extends GrantedAuthority> authorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getRole().toString()))
            .toList();

        final Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getEmail(),
            null,
            authorities
        );
        final String accessToken = tokenProvider.generateAccessToken(authentication);
        final String generatedRefreshToken = tokenProvider.generateRefreshToken();

        refreshTokenRedisRepository.deleteById(user.getEmail());
        refreshTokenRedisRepository.save(
            RefreshToken.builder().username(authentication.getName()).token(generatedRefreshToken)
                .expiration(jwtProperties.getRefreshTokenExpiration() / 1000)
                .build());

        return new LoginResponseDto(accessToken, generatedRefreshToken);
    }

    public LogoutResponseDto processLogout(final String accessToken) {
        tokenProvider.validToken(accessToken);

        final Authentication authentication = tokenProvider.getAuthentication(accessToken);

        final String userEmail = authentication.getName();

        if (redisTemplate.opsForValue().get(userEmail)
            != null) {
            redisTemplate.delete(userEmail);
        }
        final Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, "logout", expiration,
            TimeUnit.MILLISECONDS);
        refreshTokenRedisRepository.deleteById(userEmail);

        return new LogoutResponseDto(accessToken, tokenProvider.getExpiration(accessToken));
    }

    private Authentication createAuthentication(final LoginRequestDto loginRequestDto) {
        final Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
            loginRequestDto.email(), loginRequestDto.password());
        return authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);
    }

}
