package com.bidnamu.bidnamubackend.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refreshToken")
@Builder
@AllArgsConstructor
@Getter
public class RefreshToken {
    @Id
    private String username;
    @Indexed
    private String token;
    @TimeToLive
    private Long expiration;

}
