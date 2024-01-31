package com.bidnamu.bidnamubackend.auction.domain;

import com.querydsl.jpa.impl.JPAQuery;

import static com.bidnamu.bidnamubackend.auction.domain.QAuction.auction;

public enum AuctionSortMethod {
    BIDDER_COUNT_ASC {
        public void sort(JPAQuery<Auction> query) {
            query.orderBy(auction.bidders.size().asc());
        }
    },
    BIDDER_COUNT_DESC {
        public void sort(JPAQuery<Auction> query) {
            query.orderBy(auction.bidders.size().desc());
        }
    },
    CLOSING_TIME_ASC {
        public void sort(JPAQuery<Auction> query) {
            query.orderBy(auction.closingTime.asc());
        }
    },
    CLOSING_TIME_DESC {
        public void sort(JPAQuery<Auction> query) {
            query.orderBy(auction.closingTime.desc());
        }
    },
    CURRENT_BID_ASC {
        public void sort(JPAQuery<Auction> query) {
            query.orderBy(auction.currentBid.asc());
        }
    },
    CURRENT_BID_DESC {
        public void sort(JPAQuery<Auction> query) {
            query.orderBy(auction.currentBid.desc());
        }
    };

    public abstract void sort(JPAQuery<Auction> query);
}
