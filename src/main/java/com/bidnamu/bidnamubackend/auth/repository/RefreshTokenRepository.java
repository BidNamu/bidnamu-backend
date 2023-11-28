package com.bidnamu.bidnamubackend.auth.repository;

import com.bidnamu.bidnamubackend.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByUserId(Long userId);

  Optional<RefreshToken> findByRefreshTokenValue(String refreshToken);
}
