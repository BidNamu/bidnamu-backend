package com.bidnamu.bidnamubackend.user.repository;

import com.bidnamu.bidnamubackend.user.domain.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByRefreshToken(String refreshToken);

  boolean existsUserByEmail(String email);

  boolean existsUserByNickname(String nickname);
}
