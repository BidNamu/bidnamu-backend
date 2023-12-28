package com.bidnamu.bidnamubackend.user.exception;

public class UnknownUserException extends IllegalArgumentException {

  public UnknownUserException(final String message) {
    super(message);
  }
}
