package com.bidnamu.bidnamubackend.auth.service;

import com.bidnamu.bidnamubackend.auth.domain.Authority;
import com.bidnamu.bidnamubackend.auth.util.UserDetailsImpl;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
    final User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("해당 user를 찾을 수 없습니다 : " + email));
    final Set<Authority> authorities = user.getAuthorities();
    return new UserDetailsImpl(user, authorities);
  }

}
