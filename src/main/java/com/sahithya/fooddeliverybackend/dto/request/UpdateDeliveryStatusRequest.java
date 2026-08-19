package com.sahithya.fooddeliverybackend.dto.request;

import com.sahithya.fooddeliverybackend.entity.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateDeliveryStatusRequest {

    @NotNull(message = "Delivery status is required")
    private DeliveryStatus status;

    public UpdateDeliveryStatusRequest() {
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
