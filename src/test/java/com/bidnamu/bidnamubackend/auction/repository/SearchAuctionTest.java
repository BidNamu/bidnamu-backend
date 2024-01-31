package com.bidnamu.bidnamubackend.auction.repository;

import static com.bidnamu.bidnamubackend.auction.domain.AuctionSortMethod.CLOSING_TIME_DESC;
import static com.bidnamu.bidnamubackend.auction.domain.AuctionStatus.FAILURE;
import static com.bidnamu.bidnamubackend.auction.domain.AuctionStatus.ONGOING;
import static com.bidnamu.bidnamubackend.auction.domain.AuctionStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.bidnamu.bidnamubackend.auction.dto.request.SearchAuctionRequestDto;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.bidnamu.bidnamubackend.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SearchAuctionTest {

    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private List<Auction> auctions;

    @BeforeEach
    void init() {
        User seller, seller1;
        Category category, category1;
        Auction auction, auction1, auction2, auction3, auction4, auction5;

        final List<User> sellers = Arrays.asList(
            seller = new User("kimmin1@gmail.com", "kimmin1", "김민1"),
            seller1 = new User("kimmin2@gmail.com", "kimmin2", "김민2"));
        userRepository.saveAll(sellers);

        final List<Category> categories = Arrays.asList(
            category = new Category("김민1", null),
            category1 = new Category("김민2", null));
        categoryRepository.saveAll(categories);

        auctions = Arrays.asList(
            auction = new Auction("Kimmin1", "Description1", seller, category, 5000,
                LocalDateTime.now(), false),
            auction1 = new Auction("Kimmin2", "Description2", seller1, category1, 6000,
                LocalDateTime.now().minusDays(1), false),
            auction2 = new Auction("Kimmin3", "Description3", seller1, category1, 7000,
                LocalDateTime.now().minusDays(2), false),
            auction3 = new Auction("Kimmin4", "Description4", seller, category1, 8000,
                LocalDateTime.now().minusDays(3), false),
            auction4 = new Auction("Kimmin5", "Description5", seller1, category, 9000,
                LocalDateTime.now().minusDays(4), false),
            auction5 = new Auction("Kimmin6", "Description6", seller, category1, 10000,
                LocalDateTime.now().minusDays(5), false));

        auction.updateStatus(ONGOING);
        auction.updateCurrentBid(5000);
        auction1.updateStatus(SUCCESS);
        auction1.updateCurrentBid(6000);
        auction2.updateStatus(FAILURE);
        auction2.updateCurrentBid(7000);
        auction3.updateStatus(SUCCESS);
        auction3.updateCurrentBid(8000);
        auction4.updateStatus(SUCCESS);
        auction4.updateCurrentBid(9000);
        auction5.updateStatus(FAILURE);
        auction5.updateCurrentBid(10000);
        auctionRepository.saveAll(auctions);
    }

    @Test
    @DisplayName("현재 입찰 금액 범위에 해당하는 경매 검색 시 성공적으로 정보를 반환해야한다.")
    void testSearchAuction_currentBid_range() {
        //given
        final SearchAuctionRequestDto requestDto = new SearchAuctionRequestDto("", "",
            6000, 10000, null, null, null, null, 0);
        final Pageable pageable = PageRequest.of(requestDto.pageNumber(), 10);

        //when
        final Page<Auction> result = auctionRepository.findBySearchAuction(requestDto,
            pageable);

        //then
        assertEquals(5, result.getContent().size());
    }

    @Test
    @DisplayName("경매 상태에 해당하는 경매 검색을 성공적으로 정보를 반환해야한다.")
    void testSearchAuction_status() {
        //given
        final SearchAuctionRequestDto requestDto = new SearchAuctionRequestDto("", "",
            0, 0, SUCCESS, null, null, null, 0);
        final Pageable pageable = PageRequest.of(requestDto.pageNumber(), 10);

        //when
        final Page<Auction> result = auctionRepository.findBySearchAuction(requestDto,
            pageable);

        //then
        assertEquals(3, result.getContent().size());
    }

    @Test
    @DisplayName("마감일 기준으로 정렬된 경매 검색 성공적으로 정보를 반환해야한다.")
    void testSearchAuction_sort_closingTime() {
        //given
        final SearchAuctionRequestDto requestDto = new SearchAuctionRequestDto("", "",
            0, 0, null, CLOSING_TIME_DESC, null, null, 0);
        final Pageable pageable = PageRequest.of(requestDto.pageNumber(), 10);

        //when
        final Page<Auction> result = auctionRepository.findBySearchAuction(requestDto,
            pageable);

        //then
        List<Auction> sortedAuctions = auctions.stream()
            .sorted(Comparator.comparing(Auction::getClosingTime, Comparator.reverseOrder()))
            .collect(Collectors.toList());

        assertIterableEquals(sortedAuctions, result.getContent());
    }

    @Test
    @DisplayName("마감일 범위에 해당하는 경매 검색 시 성공적으로 정보를 반환해야 한다.")
    void testSearchAuction_closingTime_range() {
        //given
        final SearchAuctionRequestDto requestDto = new SearchAuctionRequestDto("", "",
            0, 0, null, null, LocalDateTime.now().minusDays(3), LocalDateTime.now(), 0);
        final Pageable pageable = PageRequest.of(requestDto.pageNumber(), 10);

        //when
        final Page<Auction> result = auctionRepository.findBySearchAuction(requestDto,
            pageable);

        //then
        assertEquals(3, result.getContent().size());
    }

    @Test
    @DisplayName("경매 제목에 해당하는 경매를 성공적으로 정보를 반환해야 한다.")
    void testSearchAuction_title() {
        //given
        final SearchAuctionRequestDto requestDto = new SearchAuctionRequestDto("", "Kimmin2",
            0, 0, null, null, null, null, 0);
        final Pageable pageable = PageRequest.of(requestDto.pageNumber(), 10);

        //when
        final Page<Auction> result = auctionRepository.findBySearchAuction(requestDto,
            pageable);

        //then
        assertEquals(1, result.getContent().size());
    }

    @Test
    @DisplayName("카테고리명에 해당하는 경매 검색 시 성공적으로 정보를 반환해야 한다.")
    void testSearchAuction_categoryName() {
        //given
        final SearchAuctionRequestDto requestDto = new SearchAuctionRequestDto("김민1", "",
            0, 0, null, null, null, null, 0);
        final Pageable pageable = PageRequest.of(requestDto.pageNumber(), 10);

        //when
        final Page<Auction> result = auctionRepository.findBySearchAuction(requestDto,
            pageable);

        //then
        assertEquals(2, result.getContent().size());
    }

    @Test
    @DisplayName("페이징 및 페이지 크기 검증 시 성공적으로 정보를 반환해야 한다.")
    void testPaginationAndPageSize() {
        //given
        final SearchAuctionRequestDto requestDto = new SearchAuctionRequestDto("", "",
            0, 0, null, CLOSING_TIME_DESC, null,
            null, 0);
        final Pageable pageable = PageRequest.of(requestDto.pageNumber(), 5);

        //when
        final Page<Auction> result = auctionRepository.findBySearchAuction(requestDto, pageable);

        //then
        assertEquals(pageable.getPageSize(), result.getSize());
        assertEquals(pageable.getPageNumber(), result.getNumber());
        assertEquals(2, result.getTotalPages());
    }
}