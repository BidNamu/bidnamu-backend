package com.bidnamu.bidnamubackend.user.service;

import com.bidnamu.bidnamubackend.auth.domain.Role;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.exception.DuplicatedEmailException;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void registration(User member) {
        if (checkEmailDuplication(member.getEmail())) {
            throw new DuplicatedEmailException("이미 존재하는 이메일입니다.");
        }
        member.addAuthority(Role.USER);
        userRepository.save(member);
    }

    private boolean checkEmailDuplication(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
