package com.bidnamu.bidnamubackend.user.controller;

import com.bidnamu.bidnamubackend.user.dto.request.RegistrationRequestDto;
import com.bidnamu.bidnamubackend.user.dto.response.RegistrationResponseDto;
import com.bidnamu.bidnamubackend.user.dto.request.UserStatusUpdateRequestDto;
import com.bidnamu.bidnamubackend.user.dto.response.UserStatusUpdateResponseDto;
import com.bidnamu.bidnamubackend.user.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/email/duplicated/{email}")
    public ResponseEntity<HttpStatus> isDuplicatedEmail(@PathVariable final String email) {
        final boolean duplicated = userService.isDuplicatedEmail(email);
        return duplicated ? RESPONSE_CONFLICT : RESPONSE_OK;
    }

    @GetMapping("/nickname/duplicated/{nickname}")
    public ResponseEntity<HttpStatus> isDuplicatedNickname(@PathVariable final String nickname) {
        final boolean duplicated = userService.isDuplicatedNickname(nickname);
        return duplicated ? RESPONSE_CONFLICT : RESPONSE_OK;
    }

    @PatchMapping("/status/expired")
    public ResponseEntity<UserStatusUpdateResponseDto> updateUserStatus(
        final Principal principal
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.updateUserStatus(principal.getName(),
                UserStatusUpdateRequestDto.expireRequestDto()));
    }

    @PatchMapping("/{email}/status")
    public ResponseEntity<UserStatusUpdateResponseDto> updateUserStatusByAdmin(
        @RequestBody final UserStatusUpdateRequestDto dto, @PathVariable final String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserStatus(email, dto));
    }
}
