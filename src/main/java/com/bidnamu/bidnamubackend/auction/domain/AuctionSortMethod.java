package com.bidnamu.bidnamubackend.auction.domain;

import static com.bidnamu.bidnamubackend.auction.domain.QAuction.auction;

import com.querydsl.jpa.impl.JPAQuery;

public enum AuctionSortMethod {
    BIDDER_COUNT_ASC,
    BIDDER_COUNT_DESC,
    CURRENT_BID_ASC,
    CURRENT_BID_DESC,
    CLOSING_TIME_ASC,
    CLOSING_TIME_DESC;

    public static JPAQuery<Auction> eqSortMethod(final JPAQuery<Auction> query,
        final String sortMethod) {
        switch (sortMethod) {
            case "BIDDER_COUNT_ASC" -> query.orderBy(auction.bidders.size().asc());
            case "BIDDER_COUNT_DESC" -> query.orderBy(auction.bidders.size().desc());
            case "CURRENT_BID_ASC" -> query.orderBy(auction.currentBid.asc());
            case "CURRENT_BID_DESC" -> query.orderBy(auction.currentBid.desc());
            case "CLOSING_TIME_ASC" -> query.orderBy(auction.closingTime.asc());
            case "CLOSING_TIME_DESC" -> query.orderBy(auction.closingTime.desc());
            default -> query.orderBy(auction.createdAt.asc());
        }
        return query;
    }
}
