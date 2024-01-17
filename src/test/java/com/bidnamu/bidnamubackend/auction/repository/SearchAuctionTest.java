package com.bidnamu.bidnamubackend.auction.repository;

import static com.bidnamu.bidnamubackend.auction.domain.AuctionSortMethod.CURRENT_BID_DESC;
import static com.bidnamu.bidnamubackend.auction.domain.AuctionStatus.FAILURE;
import static com.bidnamu.bidnamubackend.auction.domain.AuctionStatus.ONGOING;
import static com.bidnamu.bidnamubackend.auction.domain.AuctionStatus.UNBIDDEN;
import static com.bidnamu.bidnamubackend.auction.domain.QAuction.auction;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.auction.domain.AuctionStatus;
import com.bidnamu.bidnamubackend.auction.domain.Category;
import com.bidnamu.bidnamubackend.auction.dto.request.SearchAuctionRequestDto;
import com.bidnamu.bidnamubackend.global.config.QuerydslConfig;
import com.bidnamu.bidnamubackend.user.domain.User;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(QuerydslConfig.class)
class SearchAuctionTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    JPAQueryFactory jpaQueryFactory;
    List<Auction> auctions;

    Page<Auction> auctionPage;

    @BeforeEach
    void init() {
        jpaQueryFactory = new JPAQueryFactory(em);
        final User seller = new User("kimmin1@gmail.com", "kimmin1", "김민1");
        final Category category = new Category("김민", null);
        em.persist(seller);
        em.persist(category);

        Auction auction1, auction2, auction3;
        auctions = Arrays.asList(
            auction1 = new Auction("Title0", "Description0", seller, category, 3000,
                LocalDateTime.parse("2024-01-09T00:00:00"), false),
            auction2 = new Auction("Title1", "Description1", seller, category, 5000,
                LocalDateTime.parse("2024-01-10T00:00:00"), false),
            auction3 = new Auction("Title2", "Description2", seller, category, 6000,
                LocalDateTime.parse("2024-01-11T00:00:00"), false));
        auction1.updateStatus(UNBIDDEN);
        auction2.updateStatus(ONGOING);
        auction3.updateStatus(FAILURE);
        auctions.forEach(em::persist);
    }

    @Test
    @DisplayName("경매 조건별 조회 테스트")
    void searchAuction() {
        //given
        final Pageable pageable = PageRequest.of(0, 10);
        final SearchAuctionRequestDto requestDto = new SearchAuctionRequestDto("", "",
            5000, 6000, ONGOING.name(), CURRENT_BID_DESC.name(),LocalDateTime.parse("2024-01-09T00:00:00"),
            LocalDateTime.parse("2024-01-11T00:00:00"));

        //when
        final List<Auction> content = jpaQueryFactory.select(auction)
            .from(auction).where(auction.title.contains(requestDto.title()),
                auction.category.name.contains(requestDto.name()),
                auction.status.eq(AuctionStatus.valueOf(requestDto.auctionStatus())),
                auction.startingBid.between(requestDto.startPrice(), requestDto.closePrice()),
                auction.closingTime.between(requestDto.startTime(), requestDto.closeTime()))
            .orderBy(auction.currentBid.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        final JPAQuery<Long> countQuery = jpaQueryFactory
            .select(auction.count())
            .from(auction)
            .where(auction.title.contains(requestDto.title()),
                auction.category.name.contains(requestDto.name()),
                auction.status.eq(AuctionStatus.valueOf(requestDto.auctionStatus())),
                auction.startingBid.between(requestDto.startPrice(), requestDto.closePrice()),
                auction.closingTime.between(requestDto.startTime(), requestDto.closeTime()));

        auctionPage = PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

        assertEquals(1, auctionPage.getContent().size());
    }
}
