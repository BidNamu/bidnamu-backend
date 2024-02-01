package com.bidnamu.bidnamubackend.bid.dto.response;

public record NotEnoughCreditResponseDto(
    int currentBalance,
    int requiredCredit
) {

}
