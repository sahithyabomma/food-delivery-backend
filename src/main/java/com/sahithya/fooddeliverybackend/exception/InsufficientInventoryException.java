package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class InsufficientInventoryException extends RuntimeException {

    public InsufficientInventoryException(
            UUID menuItemId,
            int requested,
            int available
    ) {
        super(
                "Insufficient inventory for menu item "
                        + menuItemId
                        + ". Requested: "
                        + requested
                        + ", available: "
                        + available
        );
    }
}