package com.bidnamu.bidnamubackend.user.exception;

public class DuplicatedEmailException extends IllegalArgumentException {
    public DuplicatedEmailException(String message) {
        super(message);
    }
}
