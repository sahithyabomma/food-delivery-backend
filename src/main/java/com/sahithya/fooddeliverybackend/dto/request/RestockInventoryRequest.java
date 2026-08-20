package com.sahithya.fooddeliverybackend.dto.request;

import jakarta.validation.constraints.Min;

public class RestockInventoryRequest {

    @Min(
            value = 1,
            message = "Restock quantity must be at least 1"
    )
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
