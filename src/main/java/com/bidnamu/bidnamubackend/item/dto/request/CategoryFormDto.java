package com.bidnamu.bidnamubackend.item.dto.request;

import lombok.Builder;

@Builder
public record CategoryFormDto(String name, Long parent) {

}
