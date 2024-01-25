package com.bidnamu.bidnamubackend.auction.service;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.auction.domain.AuctionImage;
import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.bidnamu.bidnamubackend.auction.dto.CreateAuctionDto;
import com.bidnamu.bidnamubackend.auction.dto.ProcessBiddingDto;
import com.bidnamu.bidnamubackend.auction.dto.response.AuctionDetailResponseDto;
import com.bidnamu.bidnamubackend.auction.dto.response.BidResponseDto;
import com.bidnamu.bidnamubackend.auction.repository.AuctionRepository;
import com.bidnamu.bidnamubackend.bid.domain.Bid;
import com.bidnamu.bidnamubackend.bid.exception.AuctionClosedException;
import com.bidnamu.bidnamubackend.bid.exception.NotEnoughCreditException;
import com.bidnamu.bidnamubackend.bid.repository.BidRepository;
import com.bidnamu.bidnamubackend.bid.util.BidAmountValidator;
import com.bidnamu.bidnamubackend.file.domain.FileInfo;
import com.bidnamu.bidnamubackend.file.service.FileService;
import com.bidnamu.bidnamubackend.global.annotation.DistributedLock;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FileService fileService;
    private final BidRepository bidRepository;

    @Transactional
    public AuctionDetailResponseDto createAuction(final CreateAuctionDto createAuctionDto) {
        final User seller = userService.findByEmail(createAuctionDto.seller());
        final Category category = categoryService.findCategoryById(createAuctionDto.categoryId());
        categoryService.validSubCategory(category);

        final Auction auction = auctionRepository.save(createAuctionDto.toEntity(seller, category));
        // 경매 이미지 처리
        final List<FileInfo> uploadedFileInfos = fileService.uploadFiles(createAuctionDto.images());
        uploadedFileInfos.forEach(fileInfo -> auction.addAuctionImage(
            AuctionImage.builder().auction(auction).fileInfo(fileInfo).build()));
        return AuctionDetailResponseDto.from(auction);
    }

    @DistributedLock(key = "'auction'.concat(#dto.auctionId)")
    public BidResponseDto processBidding(final ProcessBiddingDto dto) {
        final User bidder = userService.findByEmail(dto.username());
        final Auction auction = findAuctionById(dto.auctionId());
        validateAuctionIsOpen(auction);
        BidAmountValidator.validBidAmount(auction.getCurrentBid(), dto.bidAmount());

        final Bid bid = createOrUpdateBid(bidder, auction, dto.bidAmount());

        return BidResponseDto.from(bid);
    }

    public Auction findAuctionById(final long auctionId) {
        return auctionRepository.findById(auctionId)
            .orElseThrow(() -> new NoSuchElementException("해당 경매를 찾을 수 없습니다 : " + auctionId));
    }

    private void validateAuctionIsOpen(final Auction auction) {
        if (!auction.isOnGoing() || auction.isAuctioned()) {
            throw new AuctionClosedException();
        }
    }

    private Bid createOrUpdateBid(
        final User bidder,
        final Auction auction,
        final int bidAmount
    ) {
        if (bidder.getCredit() - bidAmount < 0) {
            throw new NotEnoughCreditException();
        }

        auction.updateCurrentBid(bidAmount);
        if (!bidRepository.existsByBidderAndAuction(bidder, auction)) {
            auction.addBidderCount();
            return bidRepository.save(new Bid(bidder, auction, bidAmount));
        }

        Bid existingBid = bidRepository.findByBidderAndAuction(bidder, auction);
        existingBid.updateOfferAmount(bidAmount);
        return existingBid;
    }
}
