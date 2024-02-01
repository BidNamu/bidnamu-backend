package com.bidnamu.bidnamubackend.bid.repository;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.bid.domain.Bid;
import com.bidnamu.bidnamubackend.user.domain.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BidRepository extends CrudRepository<Bid, Long> {

    boolean existsByBidderAndAuction(final User bidder, final Auction auction);

    Bid findByBidderAndAuction(final User bidder, final Auction auction);

    List<Bid> findBidsByAuctionAndCurrentBidder(
        final Auction auction,
        final boolean currentBidder
    );
}
