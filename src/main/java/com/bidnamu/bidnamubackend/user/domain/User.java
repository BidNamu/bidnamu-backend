package com.bidnamu.bidnamubackend.user.domain;

import com.bidnamu.bidnamubackend.auth.domain.Authority;
import com.bidnamu.bidnamubackend.auth.domain.Role;
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
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private final Set<Authority> authorities = new HashSet<>();

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void addAuthority(Role role) {
        if (!this.authorities.isEmpty() && (authorities.stream().anyMatch(authority -> authority.getRole().equals(role)))) {
                throw new IllegalArgumentException("해당 유저는 이미 해당 권한을 가지고 있습니다.");
        }
            Authority authority = new Authority();
            authority.setRole(role);
            authority.setUser(this);
            this.authorities.add(authority);
    }
}
