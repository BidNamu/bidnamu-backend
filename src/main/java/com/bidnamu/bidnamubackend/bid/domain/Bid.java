package com.bidnamu.bidnamubackend.bid.domain;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.global.domain.BaseTimeEntity;
import com.bidnamu.bidnamubackend.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Bid extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User bidder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Auction auction;

    @Column(nullable = false)
    private int offerAmount;

    @Column(nullable = false)
    private boolean currentBidder;

    public void updateOfferAmount(final int offerAmount) {
        this.offerAmount = offerAmount;
    }

    public void updateCurrentBidder(final boolean currentBidder) {
        this.currentBidder = currentBidder;
    }
}
