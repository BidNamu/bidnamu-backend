package com.bidnamu.bidnamubackend.bid.exception;

public class AuctionClosedException extends BidException {

    public AuctionClosedException() {
        super("경매가 이미 종료되었습니다.");
    }
}
