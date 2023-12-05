package com.bidnamu.bidnamubackend.auction.repository;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import org.springframework.data.repository.CrudRepository;

public interface AuctionRepository extends CrudRepository<Auction, Long> {

}
