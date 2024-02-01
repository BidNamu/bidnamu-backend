package com.bidnamu.bidnamubackend.auction.dto.response;

import com.bidnamu.bidnamubackend.bid.domain.Bid;
import java.time.LocalDateTime;

public record BidResponseDto(
    long bidId,
    int bidAmount,
    LocalDateTime bidTime
) {

    public static BidResponseDto from(final Bid bid) {
        return new BidResponseDto(bid.getId(), bid.getOfferAmount(), bid.getUpdatedAt());
    }
}
