package com.sahithya.fooddeliverybackend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AssignDeliveryPartnerRequest {

    @NotNull(message = "Delivery partner id is required")
    private UUID deliveryPartnerId;

    public AssignDeliveryPartnerRequest() {
    }

    public UUID getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(UUID deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }
}