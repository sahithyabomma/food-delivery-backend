package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class InventoryNotFoundException
        extends RuntimeException {

    public InventoryNotFoundException(UUID menuItemId) {
        super(
                "Inventory not found for menu item: "
                        + menuItemId
        );
    }
}
