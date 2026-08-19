package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.DeliveryStatus;

import java.time.Instant;
import java.util.UUID;

public class DeliveryAssignmentResponse {

    private final UUID assignmentId;
    private final UUID orderId;
    private final UUID deliveryPartnerId;
    private final DeliveryStatus status;
    private final Instant assignedAt;
    private final Instant pickedUpAt;
    private final Instant deliveredAt;

    public DeliveryAssignmentResponse(
            UUID assignmentId,
            UUID orderId,
            UUID deliveryPartnerId,
            DeliveryStatus status,
            Instant assignedAt,
            Instant pickedUpAt,
            Instant deliveredAt
    ) {
        this.assignmentId = assignmentId;
        this.orderId = orderId;
        this.deliveryPartnerId = deliveryPartnerId;
        this.status = status;
        this.assignedAt = assignedAt;
        this.pickedUpAt = pickedUpAt;
        this.deliveredAt = deliveredAt;
    }

    public UUID getAssignmentId() {
        return assignmentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public Instant getPickedUpAt() {
        return pickedUpAt;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }
}