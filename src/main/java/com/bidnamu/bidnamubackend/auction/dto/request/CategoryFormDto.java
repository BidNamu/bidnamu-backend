package com.bidnamu.bidnamubackend.auction.dto.request;

import lombok.Builder;

@Builder
public record CategoryFormDto(String name, Long parent) {

}
