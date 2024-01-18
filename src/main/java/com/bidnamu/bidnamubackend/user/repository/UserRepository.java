package com.bidnamu.bidnamubackend.user.repository;

import com.bidnamu.bidnamubackend.user.domain.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByEmail(final String email);

  boolean existsUserByEmail(final String email);

  boolean existsUserByNickname(final String nickname);
}
