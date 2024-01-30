package com.bidnamu.bidnamubackend.credit.domain;

import lombok.Getter;

@Getter
public enum CreditChangeReason {
    CHARGE(true),
    SALE(true),
    BID(false),
    REFUND(true),
    BID_FAILED_REFUND(true);

    private final boolean increment;

    CreditChangeReason(final boolean increment) {
        this.increment = increment;
    }
}
