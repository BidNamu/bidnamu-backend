package com.bidnamu.bidnamubackend.auth.controller;

import com.bidnamu.bidnamubackend.auth.dto.CreateAccessTokenRequest;
import com.bidnamu.bidnamubackend.auth.dto.CreateAccessTokenResponse;
import com.bidnamu.bidnamubackend.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tokens")
public class TokenApiController {

  private final TokenService tokenService;

  @PostMapping
  public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(
      @RequestBody CreateAccessTokenRequest request) {
    String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateAccessTokenResponse(newAccessToken));
  }

}
