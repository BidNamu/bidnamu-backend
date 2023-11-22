package com.bidnamu.bidnamubackend.global.annotation.validation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@NotBlank(message = "공백 닉네임은 허용되지 않습니다.")
@Min(value = 5, message = "최소 5자 이상 입력해주세요")
public @interface Nickname {

}
