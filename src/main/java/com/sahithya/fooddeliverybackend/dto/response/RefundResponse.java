package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class RefundResponse {

    private final UUID refundId;
    private final UUID orderId;
    private final UUID paymentId;
    private final BigDecimal amount;
    private final RefundStatus status;
    private final String reason;
    private final String gatewayRefundReference;
    private final Instant createdAt;

    public RefundResponse(
            UUID refundId,
            UUID orderId,
            UUID paymentId,
            BigDecimal amount,
            RefundStatus status,
            String reason,
            String gatewayRefundReference,
            Instant createdAt
    ) {
        this.refundId = refundId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
        this.reason = reason;
        this.gatewayRefundReference = gatewayRefundReference;
        this.createdAt = createdAt;
    }

    public UUID getRefundId() {
        return refundId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getGatewayRefundReference() {
        return gatewayRefundReference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}