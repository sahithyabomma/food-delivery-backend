package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.OrderPaymentState;
import com.sahithya.fooddeliverybackend.entity.OrderStatus;
import com.sahithya.fooddeliverybackend.entity.RefundStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class OrderSummaryResponse {

    private final UUID orderId;
    private final String restaurantName;
    private final OrderStatus status;
    private final BigDecimal totalAmount;
    private final Instant createdAt;
    private final OrderPaymentState paymentState;
    private final RefundStatus refundStatus;

    public OrderPaymentState getPaymentState() {
        return paymentState;
    }

    public OrderSummaryResponse(
            UUID orderId,
            String restaurantName,
            OrderStatus status,
            BigDecimal totalAmount,
            Instant createdAt, OrderPaymentState paymentState, RefundStatus refundStatus
    ) {
        this.orderId = orderId;
        this.restaurantName = restaurantName;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.paymentState = paymentState;
        this.refundStatus = refundStatus;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }
}