package com.bidnamu.bidnamubackend.item.dto.request;

import lombok.Builder;

@Builder
public record CategoryFormDto(Long id, String name, Long parent) {

}
