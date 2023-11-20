package com.bidnamu.bidnamubackend.global.annotation.validation;

import jakarta.validation.constraints.Pattern;

@Pattern(message = "최소 한개 이상의 대소문자와 숫자, 특수문자를 포함한 8자 이상 16자 이하의 비밀번호를 입력해야 합니다.",
        regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!~$%^=()])(?=\\S+$).{8,16}$")
public @interface Password {
}
