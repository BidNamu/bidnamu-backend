package com.bidnamu.bidnamubackend.user.exception;

public class UnknownUserException extends IllegalArgumentException {

  public UnknownUserException(String message) {
    super(message);
  }
}
