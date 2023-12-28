package com.bidnamu.bidnamubackend.auth.exception;

public class UnknownTokenException extends IllegalArgumentException{

  public UnknownTokenException(final String message) {
    super(message);
  }
}
