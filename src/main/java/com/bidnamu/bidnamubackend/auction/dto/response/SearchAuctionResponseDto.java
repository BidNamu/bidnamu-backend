package com.bidnamu.bidnamubackend.auction.dto.response;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import java.time.LocalDateTime;
import java.util.List;

public record SearchAuctionResponseDto(
    String title,
    String description,
    String seller,
    String category,
    int startingBid,
    LocalDateTime closingTime,
    int bidderCount,
    List<String> images
) {

    public static SearchAuctionResponseDto from(final Auction auction) {
        return new SearchAuctionResponseDto(
            auction.getTitle(),
            auction.getDescription(),
            auction.getSeller().getEmail(),
            auction.getCategory().getName(),
            auction.getStartingBid(),
            auction.getClosingTime(),
            auction.getBidderCount(),
            auction.getImageOriginalFileNames()
        );
    }
}
