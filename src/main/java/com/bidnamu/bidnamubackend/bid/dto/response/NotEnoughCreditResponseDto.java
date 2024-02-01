package com.bidnamu.bidnamubackend.bid.dto.response;

import java.io.Serializable;

public record NotEnoughCreditResponseDto(
    int currentBalance,
    int requiredCredit
) implements Serializable {

}
