package com.bidnamu.bidnamubackend.auction.dto;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.bidnamu.bidnamubackend.auction.dto.request.CreateAuctionRequestDto;
import com.bidnamu.bidnamubackend.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record CreateAuctionDto(
    String title,
    String description,
    String seller,
    Long categoryId,
    int startingBid,
    LocalDateTime closingTime,
    boolean fixedPrice,
    List<MultipartFile> images
) {

    public static CreateAuctionDto of(
        final CreateAuctionRequestDto requestDto,
        final String seller,
        final List<MultipartFile> images) {
        return new CreateAuctionDto(
            requestDto.title(),
            requestDto.description(),
            seller,
            requestDto.categoryId(),
            requestDto.startingBid(),
            requestDto.closingTime(),
            requestDto.fixedPrice(),
            images
        );
    }

    public Auction toEntity(final User seller, final Category category) {
        return Auction.builder()
            .title(title)
            .description(description)
            .seller(seller)
            .category(category)
            .startingBid(startingBid)
            .closingTime(closingTime)
            .fixedPrice(fixedPrice)
            .build();
    }
}
