package com.bidnamu.bidnamubackend.bid.exception;

public class NotEnoughCreditException extends BidException {

    public NotEnoughCreditException() {
        super("입찰에 필요한 크레딧이 충분하지 않습니다.");
    }
}
