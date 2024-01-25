package com.bidnamu.bidnamubackend.auction.controller;


import com.bidnamu.bidnamubackend.auction.dto.CreateAuctionDto;
import com.bidnamu.bidnamubackend.auction.dto.ProcessBiddingDto;
import com.bidnamu.bidnamubackend.auction.dto.request.BidRequestDto;
import com.bidnamu.bidnamubackend.auction.dto.request.CreateAuctionRequestDto;
import com.bidnamu.bidnamubackend.auction.dto.response.AuctionDetailResponseDto;
import com.bidnamu.bidnamubackend.auction.dto.response.BidResponseDto;
import com.bidnamu.bidnamubackend.auction.service.AuctionService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<AuctionDetailResponseDto> createAuction(
        final Principal principal,
        @RequestPart final List<MultipartFile> images,
        @RequestPart(name = "body") @Valid final CreateAuctionRequestDto requestDto) {
        final AuctionDetailResponseDto result = auctionService.createAuction(
            CreateAuctionDto.of(requestDto, principal.getName(), images)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{auctionId}/bids")
    @PreAuthorize("hasRole('BIDDER')")
    public ResponseEntity<BidResponseDto> createBids(
        @PathVariable final long auctionId,
        final Principal principal,
        @RequestBody final BidRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(auctionService.processBidding(
            ProcessBiddingDto.of(principal.getName(), auctionId, requestDto)));
    }
}
