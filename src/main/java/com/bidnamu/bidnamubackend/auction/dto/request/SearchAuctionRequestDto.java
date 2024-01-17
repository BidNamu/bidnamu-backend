package com.bidnamu.bidnamubackend.auction.dto.request;

import java.time.LocalDateTime;

public record SearchAuctionRequestDto(
    String name,
    String title,
    int startPrice,
    int closePrice,
    String auctionStatus,
    String sortMethod,
    LocalDateTime startTime,
    LocalDateTime closeTime
) {
    public static SearchAuctionRequestDto of(
        final String name,
        final String title,
        final String auctionStatus,
        final String sortMethod,
        final LocalDateTime startTime,
        final LocalDateTime closeTime
    ) {
        return new SearchAuctionRequestDto(name, title, 0, 0,auctionStatus,sortMethod,startTime, closeTime);
    }
}
