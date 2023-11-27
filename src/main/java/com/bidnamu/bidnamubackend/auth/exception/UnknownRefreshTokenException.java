package com.bidnamu.bidnamubackend.auth.exception;

public class UnknownRefreshTokenException extends IllegalArgumentException{

  public UnknownRefreshTokenException(String message) {
    super(message);
  }
}
