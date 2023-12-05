package com.bidnamu.bidnamubackend.auction.dto.response;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import java.time.LocalDateTime;
import java.util.List;

public record AuctionDetailResponseDto(
    Long id,
    String title,
    String description,
    String category,
    LocalDateTime closingTime,
    LocalDateTime createdAt,
    List<String> images
) {

    public static AuctionDetailResponseDto from(final Auction auction) {
        return new AuctionDetailResponseDto(
            auction.getId(),
            auction.getTitle(),
            auction.getDescription(),
            auction.getCategoryName(),
            auction.getClosingTime(),
            auction.getCreatedAt(),
            auction.getImageOriginalFileNames()
        );
    }
}
