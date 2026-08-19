package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class DeliveryAssignmentNotFoundException
        extends RuntimeException {

    public DeliveryAssignmentNotFoundException(UUID orderId) {
        super(
                "Delivery assignment not found for order: "
                        + orderId
        );
    }
}