package com.bidnamu.bidnamubackend.user.exception;

public class DuplicatedNicknameException extends IllegalArgumentException {
    public DuplicatedNicknameException(String message) {
        super(message);
    }
}