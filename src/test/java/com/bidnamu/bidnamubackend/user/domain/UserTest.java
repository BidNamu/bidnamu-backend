package com.bidnamu.bidnamubackend.user.domain;

import com.bidnamu.bidnamubackend.auth.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


 class UserTest {
    private User user;
    @BeforeEach
    void setUp() {
        this.user = User.builder().email("rudals1888@gmail.com")
                .nickname("kimmin")
                .password("1234")
                .build();
        this.user.addAuthority(Role.ROLE_USER);
    }

    @Test
    void testAddAuthority() {
        assertEquals(1, user.getAuthorities().size());
    }

    @Test
    void testAddMoreAuthority() {
        user.addAuthority(Role.ROLE_ADMIN);
        assertEquals(2, user.getAuthorities().size());
    }

    @Test
    void testAddSameAuthority() {
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            user.addAuthority(Role.ROLE_USER);
        });
        assertEquals("해당 유저는 이미 해당 권한을 가지고 있습니다.", exception.getMessage());
    }

}
