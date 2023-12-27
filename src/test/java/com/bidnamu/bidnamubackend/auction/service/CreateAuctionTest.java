package com.bidnamu.bidnamubackend.auction.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.bidnamu.bidnamubackend.auction.dto.CreateAuctionDto;
import com.bidnamu.bidnamubackend.auction.exception.InvalidCategoryDepthException;
import com.bidnamu.bidnamubackend.auction.repository.AuctionRepository;
import com.bidnamu.bidnamubackend.file.domain.FileInfo;
import com.bidnamu.bidnamubackend.file.service.FileService;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.service.UserService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class CreateAuctionTest {

    @Mock
    private FileService fileService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuctionService auctionService;

    private User seller;
    private List<MultipartFile> images;
    private List<FileInfo> fileInfos;
    private Category mockCategory;
    private Auction auction;

    @BeforeEach
    void setUp() {
        setUpUser();
        setUpImages();
        mockCategory = mock();
        setUpAuction();
    }

    @Test
    @DisplayName("경매 등록 시 성공적으로 정보를 반환하여야 한다")
    void testCreateAuction_success() {
        // Given
        final CreateAuctionDto dto = new CreateAuctionDto("title", "description", "seller", 1L, 0,
            LocalDateTime.now(), false, images);

        // When
        when(userService.findByEmail(any())).thenReturn(seller);
        when(fileService.uploadFiles(any())).thenReturn(fileInfos);
        when(categoryService.findCategoryById(any())).thenReturn(mockCategory);
        when(mockCategory.getName()).thenReturn("category");
        when(auctionRepository.save(any())).thenReturn(auction);

        // Then
        assertEquals("title", auctionService.createAuction(dto).title());
    }

    @Test
    @DisplayName("카테고리가 서브 카테고리가 아닐 시 예외를 던져야 한다")
    void testCreateAuction_InvalidCategoryDepthException() {
        // Given
        final CreateAuctionDto dto = new CreateAuctionDto("title", "description", "seller", 1L, 0,
            LocalDateTime.now(), false, images);

        // When
        when(userService.findByEmail(any())).thenReturn(seller);
        when(categoryService.findCategoryById(any())).thenThrow(
            new InvalidCategoryDepthException(""));

        // Then
        assertThrows(InvalidCategoryDepthException.class, () -> auctionService.createAuction(dto));
    }

    private void setUpUser() {
        seller = User.builder().email("seller@seller.com").password("password").nickname("seller")
            .build();
    }

    private void setUpImages() {
        images = Collections.singletonList(
            new MockMultipartFile("image", "image.png", "image/png", "test content".getBytes()));
        fileInfos = Collections.singletonList(
            new FileInfo("image", "image.png", "png", 12L, "image/png"));
    }

    private void setUpAuction() {
        auction = new Auction("title", "description", seller, mockCategory, 0, LocalDateTime.now(),
            false);
    }
}
