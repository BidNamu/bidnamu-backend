package com.bidnamu.bidnamubackend.user.controller;

import com.bidnamu.bidnamubackend.user.dto.RegistrationRequestDto;
import com.bidnamu.bidnamubackend.user.dto.RegistrationResponseDto;
import com.bidnamu.bidnamubackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bidnamu.bidnamubackend.global.util.HttpStatusResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<RegistrationResponseDto> createUser(
        @RequestBody @Valid final RegistrationRequestDto registrationForm) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            userService.createUser(registrationForm));
    }

    @GetMapping("/duplicated/{email}")
    public ResponseEntity<HttpStatus> isDuplicatedEmail(@PathVariable final String email) {
        boolean duplicated = userService.isDuplicatedEmail(email);
        return duplicated ? RESPONSE_CONFLICT : RESPONSE_OK;
    }
}
