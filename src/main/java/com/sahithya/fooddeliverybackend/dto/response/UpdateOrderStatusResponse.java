package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.OrderStatus;

import java.time.Instant;
import java.util.UUID;

public class UpdateOrderStatusResponse {

    private final UUID orderId;
    private final OrderStatus status;
    private final Instant updatedAt;

    public UpdateOrderStatusResponse(
            UUID orderId,
            OrderStatus status,
            Instant updatedAt
    ) {
        this.orderId = orderId;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}