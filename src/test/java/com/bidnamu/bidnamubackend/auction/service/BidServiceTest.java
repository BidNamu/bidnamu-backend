package com.bidnamu.bidnamubackend.auction.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import java.lang.reflect.Field;
import java.time.LocalDateTime;

import java.util.List;
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
        when(bid.getBidder()).thenReturn(bidder);

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
        when(bid.getBidder()).thenReturn(bidder);

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

    @Test
    @DisplayName("즉결 경매에 입찰을 진행할 경우 경매가 종료되어야 한다")
    void givenBidOnBuyItNowAuction_whenBidIsPlaced_thenAuctionShouldBeClosed() {
        // Given
        final User bidder = mock();
        final int bidAmount = 10000;
        final Bid bid = mock();

        auction = Auction.builder()
            .startingBid(bidAmount)
            .closingTime(LocalDateTime.now().plusDays(1L))
            .fixedPrice(true)
            .build();

        // When
        when(userService.findByEmail(any())).thenReturn(bidder);
        when(bidRepository.save(any())).thenReturn(bid);
        when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));

        when(bid.getId()).thenReturn(1L);
        when(bid.getOfferAmount()).thenReturn(bidAmount);
        when(bid.getUpdatedAt()).thenReturn(LocalDateTime.now());
        when(bid.getBidder()).thenReturn(bidder);

        when(bidder.getCredit()).thenReturn(bidAmount);

        auctionService.processBidding(new ProcessBiddingDto("username", 1L, bidAmount));

        // Then
        assertTrue(auction.isAuctioned());
    }

    @Test
    @DisplayName("입찰을 진행할 때 기존 입찰이 존재할 경우 기존 입찰자의 크레딧을 반환해야 한다")
    void givenExistingBid_whenNewBidIsPlaced_thenPreviousBidderCreditsShouldBeRefunded()
        throws NoSuchFieldException, IllegalAccessException {
        // Given
        final User oldBidder = User.builder().build();
        final int oldBidderCredit = oldBidder.getCredit();
        final int oldBidAmount = 5000;
        final Bid oldBid = Bid.builder()
            .offerAmount(oldBidAmount)
            .auction(auction)
            .bidder(oldBidder)
            .build();

        final User newBidder = User.builder().build();
        final int bidAmount = 10000;
        newBidder.addCredit(bidAmount);
        final int newBidderPreviousCredit = newBidder.getCredit();
        final Bid newBid = Bid.builder()
            .bidder(newBidder)
            .offerAmount(bidAmount)
            .auction(auction)
            .build();
        Field field = Bid.class.getSuperclass().getSuperclass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(newBid, 1L);

        // When
        when(userService.findByEmail(any())).thenReturn(newBidder);
        when(bidRepository.save(any())).thenReturn(newBid);
        when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));
        when(bidRepository.findBidsByAuctionAndCurrentBidder(auction, true)).thenReturn(List.of(oldBid));

        auctionService.processBidding(new ProcessBiddingDto("username", 1L, bidAmount));

        // Then
        assertEquals(oldBidderCredit + oldBidAmount, oldBidder.getCredit());
        assertEquals(newBidderPreviousCredit - bidAmount, newBidder.getCredit());
    }

    @Test
    @DisplayName("경매 종료 5분 이내에 입찰 진행 시 경매 종료 시간이 연장된다")
    void givenBidWithinLastFiveMinutes_whenBidIsPlaced_thenAuctionClosingTimeIsExtended() {
        // Given
        final User bidder = mock();
        final int bidAmount = 10000;
        final Bid bid = mock();

        auction = Auction.builder()
            .title("Test Auction 1")
            .description("Test Description 1")
            .closingTime(LocalDateTime.now().plusMinutes(1L))
            .fixedPrice(false)
            .build();

        // When
        when(userService.findByEmail(any())).thenReturn(bidder);
        when(bidRepository.existsByBidderAndAuction(any(), any())).thenReturn(false);
        when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));
        when(bidRepository.save(any())).thenReturn(bid);

        when(bid.getId()).thenReturn(1L);
        when(bid.getOfferAmount()).thenReturn(bidAmount);
        when(bid.getUpdatedAt()).thenReturn(LocalDateTime.now());
        when(bid.getBidder()).thenReturn(bidder);

        when(bidder.getCredit()).thenReturn(bidAmount);

        auctionService.processBidding(new ProcessBiddingDto("username", 1L, bidAmount));

        // Then
        assertEquals(auction.getClosingTime().getMinute(),
            LocalDateTime.now().plusMinutes(5L).getMinute());
    }
}
