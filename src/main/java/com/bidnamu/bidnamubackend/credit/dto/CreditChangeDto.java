package com.bidnamu.bidnamubackend.credit.dto;

import com.bidnamu.bidnamubackend.credit.domain.CreditChangeHistory;
import com.bidnamu.bidnamubackend.credit.domain.CreditChangeReason;
import com.bidnamu.bidnamubackend.user.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreditChangeDto(
    @NotNull
    User user,
    int amount,
    @NotNull
    CreditChangeReason reason
) {

    public CreditChangeHistory toEntity() {
        return CreditChangeHistory.builder()
            .user(user)
            .changes(amount)
            .balance(anticipateBalance())
            .reason(reason)
            .build();
    }

    public void validateAndApply() {
        if (amount == 0) {
            throw new IllegalArgumentException("크레딧 변경 금액은 0일 수 없습니다.");
        }

        if (isAmountAndReasonInconsistent()) {
            throw new IllegalArgumentException("크레딧 변경 사유와 변경액의 증감여부가 일치하지 않습니다.");
        }

        if (!reason.isIncrement() && anticipateBalance() < 0) {
            throw new IllegalArgumentException(
                "잔여 크레딧이 충분하지 않습니다. 현재 크레딧: " + user.getCredit() + ", 요청 금액: " + amount);
        }
        user.changeCredit(amount);
    }

    private boolean isAmountAndReasonInconsistent() {
        return (amount > 0 && !reason.isIncrement()) || (amount < 0 && reason.isIncrement());
    }

    private int anticipateBalance() {
        return user.getCredit() + amount;
    }
}
