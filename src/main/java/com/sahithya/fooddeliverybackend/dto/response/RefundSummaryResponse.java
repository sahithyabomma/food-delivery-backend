package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.RefundStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class RefundSummaryResponse {

    private final UUID refundId;
    private final BigDecimal amount;
    private final RefundStatus status;

    public RefundSummaryResponse(
            UUID refundId,
            BigDecimal amount,
            RefundStatus status
    ) {
        this.refundId = refundId;
        this.amount = amount;
        this.status = status;
    }

    public UUID getRefundId() {
        return refundId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public RefundStatus getStatus() {
        return status;
    }
}