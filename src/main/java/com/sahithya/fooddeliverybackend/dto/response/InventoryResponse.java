package com.sahithya.fooddeliverybackend.dto.response;

import java.util.UUID;

public class InventoryResponse {

    private final UUID menuItemId;
    private final int availableQuantity;
    private final int reservedQuantity;

    public InventoryResponse(
            UUID menuItemId,
            int availableQuantity,
            int reservedQuantity
    ) {
        this.menuItemId = menuItemId;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
    }

    public UUID getMenuItemId() {
        return menuItemId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }
}