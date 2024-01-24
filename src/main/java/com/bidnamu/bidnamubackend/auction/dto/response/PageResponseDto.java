package com.bidnamu.bidnamubackend.auction.dto.response;

public record PageResponseDto(
    int totalPages,
    long totalElements,
    int currentPage,
    int currentElements
) {
}
