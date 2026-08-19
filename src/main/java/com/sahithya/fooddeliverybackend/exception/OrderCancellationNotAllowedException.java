package com.sahithya.fooddeliverybackend.exception;

import com.sahithya.fooddeliverybackend.entity.OrderStatus;

public class OrderCancellationNotAllowedException
        extends RuntimeException {

    public OrderCancellationNotAllowedException(
            OrderStatus currentStatus
    ) {
        super(
                "Order cannot be cancelled while status is: "
                        + currentStatus
        );
    }
}