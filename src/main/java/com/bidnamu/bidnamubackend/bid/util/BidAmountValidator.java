package com.bidnamu.bidnamubackend.bid.util;

import com.bidnamu.bidnamubackend.bid.exception.BidTooLowException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BidAmountValidator {

    public static void validBidAmount(final int currentBid, final int offeredBid) {
        final int bidIncrement = calculateBidIncrement(currentBid);
        final int minimumNextBid = currentBid + bidIncrement;

        if (offeredBid < minimumNextBid) {
            throw new BidTooLowException(minimumNextBid);
        }
    }

    private static int calculateBidIncrement(int currentBid) {
        if (currentBid < 10000) {
            return 100;
        } else if (currentBid < 50000) {
            return 1000;
        } else if (currentBid < 100000) {
            return 2500;
        } else if (currentBid < 500000) {
            return 5000;
        } else {
            return 10000;
        }
    }
}
