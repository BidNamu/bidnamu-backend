package com.bidnamu.bidnamubackend.auction.repository;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.auction.dto.request.SearchAuctionRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuctionRepositoryCustom {

    Page<Auction> findBySearchAuction(final SearchAuctionRequestDto requestDto,
        final Pageable pageable);
}
