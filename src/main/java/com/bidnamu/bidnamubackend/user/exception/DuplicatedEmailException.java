package com.bidnamu.bidnamubackend.user.exception;

public class DuplicatedEmailException extends IllegalArgumentException {
    public DuplicatedEmailException(final String message) {
        super(message);
    }
}
