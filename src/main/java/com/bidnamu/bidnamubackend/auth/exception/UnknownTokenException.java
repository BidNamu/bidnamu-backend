package com.bidnamu.bidnamubackend.auth.exception;

public class UnknownTokenException extends IllegalArgumentException{

  public UnknownTokenException(String message) {
    super(message);
  }
}
