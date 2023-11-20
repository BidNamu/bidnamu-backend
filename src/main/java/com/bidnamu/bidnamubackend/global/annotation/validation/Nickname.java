package com.bidnamu.bidnamubackend.global.annotation.validation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@NotBlank
@Min(5)
public @interface Nickname {

}
