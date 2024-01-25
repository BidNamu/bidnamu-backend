package com.bidnamu.bidnamubackend.user.domain;

import com.bidnamu.bidnamubackend.auth.domain.Authority;
import com.bidnamu.bidnamubackend.auth.domain.Role;
import com.bidnamu.bidnamubackend.bid.exception.NotEnoughCreditException;
import com.bidnamu.bidnamubackend.global.domain.BaseTimeEntity;
import com.bidnamu.bidnamubackend.user.dto.request.UserStatusUpdateRequestDto;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean expired = false;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    private int credit = 0;

    @Builder
    public User(final String email, final String password, final String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,
        CascadeType.REMOVE})
    private final Set<Authority> authorities = new HashSet<>();

    public void addAuthority(final Role role) {
        if (!this.authorities.isEmpty() && (authorities.stream()
            .anyMatch(authority -> authority.getRole().equals(role)))) {
            throw new IllegalArgumentException("해당 유저는 이미 해당 권한을 가지고 있습니다.");
        }
        final Authority authority = Authority.builder().role(role).user(this).build();
        this.authorities.add(authority);
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void addCredit(final int credit) {
        this.credit += credit;
    }

    public void deductCredit(final int amount) {
        if (this.credit < amount) {
            throw new NotEnoughCreditException();
        }

        this.credit -= amount;
    }

    public void updateStatus(final UserStatusUpdateRequestDto dto) {
        if (dto.enabled() != null) {
            this.enabled = dto.enabled();
        }
        if (dto.expired() != null) {
            this.expired = dto.expired();
        }
    }
}
