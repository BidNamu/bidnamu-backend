package com.bidnamu.bidnamubackend.item.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryFormDto(@NotBlank String name, Long parent) {

}
