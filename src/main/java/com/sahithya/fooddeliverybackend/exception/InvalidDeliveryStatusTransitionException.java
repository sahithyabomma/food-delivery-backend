package com.sahithya.fooddeliverybackend.exception;

import com.sahithya.fooddeliverybackend.entity.DeliveryStatus;

public class InvalidDeliveryStatusTransitionException
        extends RuntimeException {

    public InvalidDeliveryStatusTransitionException(
            DeliveryStatus currentStatus,
            DeliveryStatus requestedStatus
    ) {
        super(
                "Cannot change delivery status from "
                        + currentStatus
                        + " to "
                        + requestedStatus
        );
    }
}