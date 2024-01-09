package com.bidnamu.bidnamubackend.credit.domain;

public enum PaymentStatus {
    PAID,
    READY,
    FAILED,
    CANCELLED;

    public static PaymentStatus of(final String status) {
        return switch (status.toLowerCase()) {
            case "paid" -> PAID;
            case "ready" -> READY;
            case "failed" -> FAILED;
            case "canceled" -> CANCELLED;
            default -> throw new IllegalArgumentException("올바른 결제 상태가 아닙니다: " + status);
        };
    }
}
