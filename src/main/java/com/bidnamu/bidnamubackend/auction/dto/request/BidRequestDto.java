package com.bidnamu.bidnamubackend.auction.dto.request;

import jakarta.validation.constraints.Min;

public record BidRequestDto(
    @Min(value = 0, message = "입찰 금액은 최소 0 이상이여야 합니다.")
    int bidAmount
) {

}
