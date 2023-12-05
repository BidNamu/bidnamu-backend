package com.bidnamu.bidnamubackend.auction.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record CreateAuctionRequestDto(
    @NotBlank(message = "공백 제목은 허용되지 않습니다.")
    String title,
    @NotBlank(message = "공백 설명은 허용되지 않습니다.")
    String description,
    @NotNull(message = "시작 가격은 반드시 지정되어야 합니다.")
    @Positive(message = "시작 가격은 반드시 양수여야 합니다.")
    int startingBid,
    @NotNull(message = "공백 카테고리 ID 는 허용되지 않습니다.")
    Long categoryId,
    @Future(message = "경매 종료 시간은 과거일 수 없습니다.")
    LocalDateTime closingTime
) {

}
