package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class DeliveryAlreadyAssignedException
        extends RuntimeException {

    public DeliveryAlreadyAssignedException(UUID orderId) {
        super(
                "A delivery partner is already assigned to order: "
                        + orderId
        );
    }
}