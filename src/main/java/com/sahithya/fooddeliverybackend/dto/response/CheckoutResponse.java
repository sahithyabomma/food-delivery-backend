package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CheckoutResponse {

    private final UUID orderId;
    private final OrderStatus status;
    private final OrderRestaurantSummaryResponse restaurant;
    private final List<OrderItemResponse> items;
    private final BigDecimal totalAmount;
    private final Instant createdAt;

    public CheckoutResponse(
            UUID orderId,
            OrderStatus status,
            OrderRestaurantSummaryResponse restaurant,
            List<OrderItemResponse> items,
            BigDecimal totalAmount,
            Instant createdAt
    ) {
        this.orderId = orderId;
        this.status = status;
        this.restaurant = restaurant;
        this.items = items;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
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
}