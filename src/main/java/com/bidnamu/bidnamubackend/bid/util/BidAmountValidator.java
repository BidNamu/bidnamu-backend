package com.bidnamu.bidnamubackend.bid.util;

import com.bidnamu.bidnamubackend.bid.exception.BidTooLowException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BidAmountValidator {

    public static final int ZERO_TO_TEN_K = 100;
    public static final int TEN_K_TO_FIFTEEN_K = 1000;
    public static final int FIFTEEN_K_TO_ONE_HUNDRED_K = 2500;
    public static final int ONE_HUNDRED_K_TO_FIVE_HUNDRED_K = 5000;
    public static final int OVER_FIVE_HUNDRED_K = 10000;

    public static void validBidAmount(final int currentBid, final int offeredBid) {
        final int bidIncrement = calculateBidIncrement(currentBid);
        final int minimumNextBid = currentBid + bidIncrement;

        if (offeredBid < minimumNextBid) {
            throw new BidTooLowException(minimumNextBid);
        }
    }

    private static int calculateBidIncrement(int currentBid) {
        if (currentBid < 10000) {
            return ZERO_TO_TEN_K;
        } else if (currentBid < 50000) {
            return TEN_K_TO_FIFTEEN_K;
        } else if (currentBid < 100000) {
            return FIFTEEN_K_TO_ONE_HUNDRED_K;
        } else if (currentBid < 500000) {
            return ONE_HUNDRED_K_TO_FIVE_HUNDRED_K;
        } else {
            return OVER_FIVE_HUNDRED_K;
        }
    }
}
