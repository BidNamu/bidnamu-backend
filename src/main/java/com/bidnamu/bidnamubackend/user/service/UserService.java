package com.bidnamu.bidnamubackend.user.service;

import com.bidnamu.bidnamubackend.auth.domain.Role;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.dto.RegistrationRequestDto;
import com.bidnamu.bidnamubackend.user.dto.RegistrationResponseDto;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedNicknameException;
import com.bidnamu.bidnamubackend.user.exception.UnknownUserException;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public RegistrationResponseDto createUser(final RegistrationRequestDto form) {
    if (isDuplicatedEmail(form.email())) {
      throw new DuplicatedEmailException("이미 존재하는 이메일입니다.");
    }

    if (isDuplicatedNickname(form.nickname())) {
      throw new DuplicatedNicknameException("이미 존재하는 닉네임입니다.");
    }

    final User user = userRepository.save(form.toEntity(passwordEncoder));
    user.addAuthority(Role.USER);
    return RegistrationResponseDto.from(user);
  }

  public User findByEmail(final String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UnknownUserException("존재하지 않는 유저입니다"));
  }

  public User findByRefreshToken(final String refreshToken) {
    return userRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new UnknownUserException("존재하지 않는 유저입니다"));
  }

  public boolean isDuplicatedEmail(final String email) {
    return userRepository.existsUserByEmail(email);
  }

  public boolean isDuplicatedNickname(final String nickname) {
    return userRepository.existsUserByNickname(nickname);
  }
}
