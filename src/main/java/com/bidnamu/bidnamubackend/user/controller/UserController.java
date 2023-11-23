package com.bidnamu.bidnamubackend.user.controller;

import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.dto.RegistrationRequestDto;
import com.bidnamu.bidnamubackend.user.dto.RegistrationResponseDto;
import com.bidnamu.bidnamubackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<RegistrationResponseDto> registration(@RequestBody @Valid RegistrationRequestDto registrationForm) {
        User member = registrationForm.toEntity(passwordEncoder);
        memberService.registration(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrationResponseDto(member));
    }
}
