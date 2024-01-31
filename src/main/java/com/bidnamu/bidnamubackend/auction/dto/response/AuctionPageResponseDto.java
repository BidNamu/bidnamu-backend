package com.bidnamu.bidnamubackend.auction.dto.response;

import java.util.List;

public record AuctionPageResponseDto(
    List<SearchAuctionResponseDto> auctions,
    PageResponseDto page
) {

    public static AuctionPageResponseDto from(final List<SearchAuctionResponseDto> auctions,
        final PageResponseDto page) {
        return new AuctionPageResponseDto(
            auctions,
            page
        );
    }
}
