package com.bidnamu.bidnamubackend.bid.exception;

public class NotEnoughPointException extends BidException {

    public NotEnoughPointException() {
        super("입찰에 필요한 포인트가 충분하지 않습니다.");
    }
}
