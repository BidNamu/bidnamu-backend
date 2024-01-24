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
    LocalDateTime closeTime,
    int pageNumber
) {

}
