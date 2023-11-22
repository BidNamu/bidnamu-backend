package com.bidnamu.bidnamubackend.user.repository;

import com.bidnamu.bidnamubackend.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
