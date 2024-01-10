package com.bidnamu.bidnamubackend.auth.repository;

import com.bidnamu.bidnamubackend.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
    RefreshToken findByToken(String token);
}
