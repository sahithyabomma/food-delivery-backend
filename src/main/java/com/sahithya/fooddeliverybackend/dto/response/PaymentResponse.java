package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.PaymentMethod;
import com.sahithya.fooddeliverybackend.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class PaymentResponse {

    private final UUID paymentId;
    private final UUID orderId;
    private final BigDecimal amount;
    private final PaymentMethod paymentMethod;
    private final PaymentStatus status;
    private final String transactionReference;
    private final Instant createdAt;

    public PaymentResponse(
            UUID paymentId,
            UUID orderId,
            BigDecimal amount,
            PaymentMethod paymentMethod,
            PaymentStatus status,
            String transactionReference,
            Instant createdAt
    ) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionReference = transactionReference;
        this.createdAt = createdAt;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}