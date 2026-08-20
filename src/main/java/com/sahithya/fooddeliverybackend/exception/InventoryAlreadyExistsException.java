package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class InventoryAlreadyExistsException
        extends RuntimeException {

    public InventoryAlreadyExistsException(UUID menuItemId) {
        super(
                "Inventory already exists for menu item: "
                        + menuItemId
        );
    }
}