package com.bidnamu.bidnamubackend.auction.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.bidnamu.bidnamubackend.auction.dto.ProcessBiddingDto;
import com.bidnamu.bidnamubackend.auction.repository.AuctionRepository;
import com.bidnamu.bidnamubackend.bid.domain.Bid;
import com.bidnamu.bidnamubackend.bid.exception.AuctionClosedException;
import com.bidnamu.bidnamubackend.bid.exception.BidTooLowException;
import com.bidnamu.bidnamubackend.bid.exception.NotEnoughCreditException;
import com.bidnamu.bidnamubackend.bid.repository.BidRepository;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.service.UserService;
import java.time.LocalDateTime;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @InjectMocks
    private AuctionService auctionService;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserService userService;

    @Mock
    private BidRepository bidRepository;

    private Auction auction;

    @BeforeEach
    void setUp() {
        final User seller = mock();
        final Category category = mock();

        auction = Auction.builder()
            .title("Test Auction 1")
            .description("Test Description 1")
            .seller(seller)
            .category(category)
            .startingBid(0)
            .closingTime(LocalDateTime.now().plusMinutes(4L))
            .fixedPrice(false)
            .build();
    }

    @Test
    @DisplayName("입찰자의 새 입찰 시 경매의 bidderCount가 1 증가하여야 한다")
    void givenAuction_whenNewBidByBidder_thenBidderCountShouldIncreaseByOne() {
        // Given
        final User bidder = mock();
        final int beforeBidderCount = auction.getBidderCount();
        final int bidAmount = 10000;
        final Bid bid = mock();

        // When
        when(userService.findByEmail(any())).thenReturn(bidder);
        when(bidRepository.existsByBidderAndAuction(any(), any())).thenReturn(false);
        when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));
        when(bidRepository.save(any())).thenReturn(bid);

        when(bid.getId()).thenReturn(1L);
        when(bid.getOfferAmount()).thenReturn(bidAmount);
        when(bid.getUpdatedAt()).thenReturn(LocalDateTime.now());

        when(bidder.getCredit()).thenReturn(bidAmount);

        auctionService.processBidding(new ProcessBiddingDto("username", 1L, bidAmount));

        // Then
        assertEquals(beforeBidderCount + 1, auction.getBidderCount());
        assertEquals(bidAmount, auction.getCurrentBid());
    }

    @Test
    @DisplayName("이미 입찰에 참여한 경매에 입찰을 추가로 진행하였을 경우 bidderCount가 증가하지 않아햐 한다")
    void givenExistingBidInAuction_whenAdditionalBidIsMade_thenBidderCountShouldNotIncrease() {
        // Given
        final User bidder = mock();
        final int beforeBidderCount = auction.getBidderCount();
        final int bidAmount = 10000;
        final Bid bid = mock();

        // When
        when(userService.findByEmail(any())).thenReturn(bidder);
        when(bidRepository.existsByBidderAndAuction(any(), any())).thenReturn(true);
        when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));
        when(bidRepository.findByBidderAndAuction(any(), any())).thenReturn(bid);

        when(bid.getId()).thenReturn(1L);
        when(bid.getOfferAmount()).thenReturn(bidAmount);
        when(bid.getUpdatedAt()).thenReturn(LocalDateTime.now());

        when(bidder.getCredit()).thenReturn(bidAmount);

        auctionService.processBidding(new ProcessBiddingDto("username", 1L, bidAmount));

        // Then
        assertEquals(beforeBidderCount, auction.getBidderCount());
        assertEquals(bidAmount, auction.getCurrentBid());
    }

    @Test
    @DisplayName("종료된 경매에 입찰을 진행할 경우 예외를 던져야 한다")
    void givenClosedAuction_whenBidIsPlaced_thenExceptionShouldBeThrown() {
        // Given
        auction = Auction.builder()
            .startingBid(0)
            .closingTime(LocalDateTime.now().minusDays(1L))
            .fixedPrice(false)
            .build();
        final ProcessBiddingDto dto = new ProcessBiddingDto("username", 1L, 10000);

        // When
        when(userService.findByEmail(any())).thenReturn(mock());
        when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));

        // Then
        assertThrows(AuctionClosedException.class, () -> auctionService.processBidding(dto));
    }

    @Test
    @DisplayName("최소 입찰가보다 작은 입찰가로 입찰을 진행할 경우 예외를 던져야 한다")
    void givenBidLowerThanMinimum_whenBidIsPlaced_thenExceptionShouldBeThrown() {
        // Given
        final ProcessBiddingDto dto =
            new ProcessBiddingDto("username", 1L, auction.getCurrentBid());

        // When
        when(userService.findByEmail(any())).thenReturn(mock());
        when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));

        // Then
        assertThrows(BidTooLowException.class, () -> auctionService.processBidding(dto));
    }

    @Test
    @DisplayName("사용자가 보유하고 있는 크레딧보다 더 많은 입찰가로 입찰을 진행할 경우 예외를 던져야 한다")
    void givenBidHigherThanUserCredits_whenBidIsPlaced_thenExceptionShouldBeThrown() {
        // Given
        final User bidder = mock();
        final int bidAmount = 10000;
        final ProcessBiddingDto dto = new ProcessBiddingDto("username", 1L, bidAmount);

        // When
        when(userService.findByEmail(any())).thenReturn(bidder);
        when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));

        when(bidder.getCredit()).thenReturn(bidAmount - 1);

        // Then
        assertThrows(NotEnoughCreditException.class, () -> auctionService.processBidding(dto));
    }
}
