package com.bidnamu.bidnamubackend.bid.exception;

public class BidTooLowException extends BidException {

    public BidTooLowException(final int minimumBid) {
        super("입찰 금액이 너무 낮습니다. 최소 입찰 금액은 " + minimumBid + "포인트 입니다.");
    }
}
