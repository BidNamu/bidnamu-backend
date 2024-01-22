package com.bidnamu.bidnamubackend.auction.repository.impl;

import static com.bidnamu.bidnamubackend.auction.domain.QAuction.auction;

import com.bidnamu.bidnamubackend.auction.domain.Auction;
import com.bidnamu.bidnamubackend.auction.domain.AuctionStatus;
import com.bidnamu.bidnamubackend.auction.dto.request.SearchAuctionRequestDto;
import com.bidnamu.bidnamubackend.auction.repository.AuctionRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

public class AuctionRepositoryImpl implements AuctionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AuctionRepositoryImpl(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Auction> findBySearchAuction(final SearchAuctionRequestDto requestDto,
        final Pageable pageable) {
        JPAQuery<Auction> query = queryFactory.selectFrom(auction)
            .where(eqTitle(requestDto.title()), eqCategoryName(requestDto.name()),
                eqAuctionStatus(requestDto.auctionStatus()),
                eqRangePrice(requestDto.startPrice(), requestDto.closePrice()),
                eqRangeTime(requestDto.startTime(), requestDto.closeTime()));

        if (requestDto.sortMethod() != null) {
            query = eqSortMethod(query, requestDto.sortMethod().name());
        }

        final List<Auction> content = query.offset(pageable.getOffset())
            .limit(pageable.getPageSize()).fetch();

        final JPAQuery<Long> countQuery = queryFactory.select(auction.count()).from(auction)
            .where(eqTitle(requestDto.title()), eqCategoryName(requestDto.name()),
                eqAuctionStatus(requestDto.auctionStatus()),
                eqRangePrice(requestDto.startPrice(), requestDto.closePrice()),
                eqRangeTime(requestDto.startTime(), requestDto.closeTime()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression eqCategoryName(final String name) {
        return StringUtils.hasText(name) ? auction.category.name.contains(name) : null;
    }

    private BooleanExpression eqTitle(final String title) {
        return StringUtils.hasText(title) ? auction.title.contains(title) : null;
    }

    private BooleanExpression eqAuctionStatus(final Enum<AuctionStatus> auctionStatus) {
        return auctionStatus != null ? auction.status.eq((AuctionStatus) auctionStatus) : null;
    }

    private BooleanExpression eqRangePrice(final int startPrice, final int closePrice) {
        return startPrice != 0 && closePrice != 0 ? auction.currentBid.between(startPrice,
            closePrice) : null;
    }

    private BooleanExpression eqRangeTime(final LocalDateTime startTime,
        final LocalDateTime closeTime) {
        return startTime != null || closeTime != null ? auction.closingTime.between(startTime,
            closeTime) : null;
    }

    private JPAQuery<Auction> eqSortMethod(final JPAQuery<Auction> query, final String sortMethod) {
        if (sortMethod != null) {
            return switch (sortMethod) {
                case "BIDDER_COUNT_ASC" -> query.orderBy(auction.bidders.size().asc());
                case "BIDDER_COUNT_DESC" -> query.orderBy(auction.bidders.size().desc());
                case "CURRENT_BID_ASC" -> query.orderBy(auction.currentBid.asc());
                case "CURRENT_BID_DESC" -> query.orderBy(auction.currentBid.desc());
                case "CLOSING_TIME_ASC" -> query.orderBy(auction.closingTime.asc());
                case "CLOSING_TIME_DESC" -> query.orderBy(auction.closingTime.desc());
                default -> query.orderBy(auction.createdAt.asc());
            };
        }
        return query;
    }
}