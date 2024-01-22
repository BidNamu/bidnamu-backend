package com.bidnamu.bidnamubackend.auction.dto.request;

import com.bidnamu.bidnamubackend.auction.domain.AuctionSortMethod;
import com.bidnamu.bidnamubackend.auction.domain.AuctionStatus;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

public record SearchAuctionRequestDto(
    String name,
    String title,
    @PositiveOrZero
    int startPrice,
    @PositiveOrZero
    int closePrice,
    AuctionStatus auctionStatus,
    AuctionSortMethod sortMethod,
    @PastOrPresent
    LocalDateTime startTime,
    @PastOrPresent
    LocalDateTime closeTime
) {

    public static SearchAuctionRequestDto of(
        final String name,
        final String title,
        final AuctionStatus auctionStatus,
        final AuctionSortMethod sortMethod,
        final LocalDateTime startTime,
        final LocalDateTime closeTime
    ) {
        return new SearchAuctionRequestDto(name, title, 0, 0, auctionStatus, sortMethod, startTime,
            closeTime);
    }
}
