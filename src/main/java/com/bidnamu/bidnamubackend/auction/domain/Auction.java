package com.bidnamu.bidnamubackend.auction.domain;

import com.bidnamu.bidnamubackend.global.domain.BaseTimeEntity;
import com.bidnamu.bidnamubackend.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auction extends BaseTimeEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User seller;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Category category;

    @Column(nullable = false)
    private int startingBid;

    @Column(nullable = false)
    private int currentBid = startingBid;

    @Column(nullable = false)
    private LocalDateTime closingTime;

    @Column(nullable = false)
    private int bidderCount = 0;

    @ToString.Exclude
    @OneToMany(mappedBy = "auction", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<AuctionImage> auctionImages = new ArrayList<>();

    @Column(nullable = false)
    private boolean fixedPrice = false;

    @Column(nullable = false)
    private boolean auctioned = false;

    @Builder
    public Auction(
        final String title,
        final String description,
        final User seller,
        final Category category,
        final int startingBid,
        final LocalDateTime closingTime,
        final boolean fixedPrice
    ) {
        this.title = title;
        this.description = description;
        this.seller = seller;
        this.category = category;
        this.startingBid = startingBid;
        this.closingTime = Objects.requireNonNullElseGet(closingTime,
            () -> LocalDateTime.now().plusWeeks(1L));
        this.fixedPrice = fixedPrice;
    }

    public void updateCurrentBid(final int currentBid) {
        this.currentBid = currentBid;
    }

    public void reevaluateClosingTime() {
        final LocalDateTime currentTime = LocalDateTime.now();
        if (closingTime.minusMinutes(5).isBefore(currentTime)) {
            closingTime = currentTime.plusMinutes(5);
        }
    }

    public void addBidderCount() {
        bidderCount++;
    }

    public void addAuctionImage(final AuctionImage auctionImage) {
        this.auctionImages.add(auctionImage);
    }

    public String getCategoryName() {
        return category.getName();
    }

    public List<String> getImageOriginalFileNames() {
        return auctionImages.stream().map(AuctionImage::getOriginalFileName).toList();
    }

    public boolean isOnGoing() {
        return LocalDateTime.now().isBefore(closingTime) && (!auctioned);
    }

    public void closeAuction() {
        this.auctioned = true;
    }
}
