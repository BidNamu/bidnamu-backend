package com.bidnamu.bidnamubackend.bid.exception;

public class BidTooLowException extends BidException {

    public BidTooLowException() {
        super("입찰 금액이 너무 낮습니다.");
    }
}
