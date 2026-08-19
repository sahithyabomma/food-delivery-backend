package com.sahithya.fooddeliverybackend.exception;

import com.sahithya.fooddeliverybackend.entity.OrderStatus;

public class InvalidOrderStatusTransitionException
        extends RuntimeException {

    public InvalidOrderStatusTransitionException(
            OrderStatus currentStatus,
            OrderStatus requestedStatus
    ) {
        super(
                "Cannot change order status from "
                        + currentStatus
                        + " to "
                        + requestedStatus
        );
    }
}