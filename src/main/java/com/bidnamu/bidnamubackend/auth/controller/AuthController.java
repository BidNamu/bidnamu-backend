package com.bidnamu.bidnamubackend.auth.controller;

import com.bidnamu.bidnamubackend.auth.dto.request.LoginRequestDto;
import com.bidnamu.bidnamubackend.auth.dto.response.LoginResponseDto;
import com.bidnamu.bidnamubackend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auths")
public class AuthController {

  private final AuthService authService;

  @PostMapping
  public ResponseEntity<LoginResponseDto> processLogin(@RequestBody LoginRequestDto requestDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.processLogin(requestDto));
  }

}
