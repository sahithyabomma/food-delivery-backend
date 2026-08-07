package com.sahithya.fooddeliverybackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AddToCartRequest {
    @NotNull(message = "Menu item is required")
    private UUID menuItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public AddToCartRequest(final UUID menuItemId,
                            final Integer quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public UUID getMenuItemId() {
        return this.menuItemId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }
}
