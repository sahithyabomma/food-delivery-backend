package com.sahithya.fooddeliverybackend.dto.request;

import jakarta.validation.constraints.Min;

public class CreateInventoryRequest {

    @Min(
            value = 0,
            message = "Available quantity cannot be negative"
    )
    private int availableQuantity;

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
