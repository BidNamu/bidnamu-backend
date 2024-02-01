package com.bidnamu.bidnamubackend.auction.dto;

import com.bidnamu.bidnamubackend.auction.dto.request.BidRequestDto;

public record ProcessBiddingDto(
    String username,
    long auctionId,
    int bidAmount
) {

    public static ProcessBiddingDto of(
        final String username,
        final long auctionId,
        final BidRequestDto bidRequestDto
    ) {
        return new ProcessBiddingDto(username, auctionId, bidRequestDto.bidAmount());
    }
}
