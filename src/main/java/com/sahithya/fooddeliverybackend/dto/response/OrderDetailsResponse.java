package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.OrderPaymentState;
import com.sahithya.fooddeliverybackend.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderDetailsResponse {

    private final UUID orderId;
    private final OrderStatus status;
    private final OrderRestaurantSummaryResponse restaurant;
    private final List<OrderItemResponse> items;
    private final BigDecimal totalAmount;
    private final Instant createdAt;
    private final OrderPaymentState paymentState;
    private final RefundSummaryResponse refund;

    public OrderDetailsResponse(
            UUID orderId,
            OrderStatus status,
            OrderRestaurantSummaryResponse restaurant,
            List<OrderItemResponse> items,
            BigDecimal totalAmount,
            Instant createdAt, OrderPaymentState paymentState, RefundSummaryResponse refund
    ) {
        this.orderId = orderId;
        this.status = status;
        this.restaurant = restaurant;
        this.items = items;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.paymentState = paymentState;
        this.refund = refund;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderRestaurantSummaryResponse getRestaurant() {
        return restaurant;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public OrderPaymentState getPaymentState() {
        return paymentState;
    }
}