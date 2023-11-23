package com.bidnamu.bidnamubackend.user.service;

import com.bidnamu.bidnamubackend.auth.domain.Role;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.dto.RegistrationRequestDto;
import com.bidnamu.bidnamubackend.user.dto.RegistrationResponseDto;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
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
        if (isExistEmail(form.email())) {
            throw new DuplicatedEmailException("이미 존재하는 이메일입니다.");
        }

        final User user = userRepository.save(form.toEntity(passwordEncoder));
        user.addAuthority(Role.USER);
        return RegistrationResponseDto.from(user);
    }

    private boolean isExistEmail(final String email) {
        return userRepository.existsUserByEmail(email);
    }

}
