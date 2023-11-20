package com.bidnamu.bidnamubackend.global.annotation.validation;


import jakarta.validation.constraints.Pattern;

@Pattern(message = "유효하지 않은 이메일 형식입니다.",
        regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
public @interface Email {

}
