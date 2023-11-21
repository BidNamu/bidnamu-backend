package com.bidnamu.bidnamubackend.user.service;

import com.bidnamu.bidnamubackend.auth.domain.Authority;
import com.bidnamu.bidnamubackend.auth.domain.Role;
import com.bidnamu.bidnamubackend.auth.repository.AuthorityRepository;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public void registration(User member) {
        Authority authority = new Authority();
        authority.setRole(Role.USER);
        authority.setUser(member);

        member.getAuthorities().add(authority);
        userRepository.save(member);
        authorityRepository.save(authority);
    }
}
