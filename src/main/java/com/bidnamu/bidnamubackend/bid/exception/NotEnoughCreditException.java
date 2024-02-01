package com.bidnamu.bidnamubackend.bid.exception;

import com.bidnamu.bidnamubackend.bid.dto.response.NotEnoughCreditResponseDto;
import lombok.Getter;

@Getter
public class NotEnoughCreditException extends BidException {

    private final NotEnoughCreditResponseDto responseDto;

    public NotEnoughCreditException(final NotEnoughCreditResponseDto responseDto) {
        super("입찰에 필요한 크레딧이 충분하지 않습니다, "
            + "현재 잔여 크레딧 : " + responseDto.currentBalance() +
            ", 필요 크레딧 : " + responseDto.requiredCredit());
        this.responseDto = responseDto;
    }
}
