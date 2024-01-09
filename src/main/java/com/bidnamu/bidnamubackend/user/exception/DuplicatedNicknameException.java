package com.bidnamu.bidnamubackend.user.exception;

public class DuplicatedNicknameException extends IllegalArgumentException {
    public DuplicatedNicknameException(final String message) {
        super(message);
    }
}