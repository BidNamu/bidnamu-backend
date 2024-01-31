package com.bidnamu.bidnamubackend.auction.service;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.auction.domain.AuctionImage;
import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.bidnamu.bidnamubackend.auction.dto.CreateAuctionDto;
import com.bidnamu.bidnamubackend.auction.dto.request.SearchAuctionRequestDto;
import com.bidnamu.bidnamubackend.auction.dto.response.AuctionDetailResponseDto;
import com.bidnamu.bidnamubackend.auction.dto.response.AuctionPageResponseDto;
import com.bidnamu.bidnamubackend.auction.dto.response.PageResponseDto;
import com.bidnamu.bidnamubackend.auction.dto.response.SearchAuctionResponseDto;
import com.bidnamu.bidnamubackend.auction.repository.AuctionRepository;
import com.bidnamu.bidnamubackend.file.domain.FileInfo;
import com.bidnamu.bidnamubackend.file.service.FileService;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FileService fileService;

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

    @Transactional(readOnly = true)
    public AuctionPageResponseDto searchAuction(final SearchAuctionRequestDto requestDto) {
        final Pageable pageable = PageRequest.of(requestDto.pageNumber(), 5);
        final Page<Auction> result = auctionRepository.findBySearchAuction(requestDto, pageable);
        final List<SearchAuctionResponseDto> auctions = result.getContent().stream()
            .map(SearchAuctionResponseDto::from)
            .toList();
        final PageResponseDto page = new PageResponseDto(result.getTotalPages(),
            result.getTotalElements(), result.getNumber(), result.getNumberOfElements());
        return AuctionPageResponseDto.from(auctions, page);
    }
}
