package com.bidnamu.bidnamubackend.user.domain;

import com.bidnamu.bidnamubackend.auth.domain.Authority;
import com.bidnamu.bidnamubackend.global.domain.BaseTimeEntity;
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

    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private final Set<Authority> authorities = new HashSet<>();

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void updateExpired(boolean expired) {
        this.expired = expired;
    }
}
