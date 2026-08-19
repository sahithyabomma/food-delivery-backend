package com.sahithya.fooddeliverybackend.exception;

import com.sahithya.fooddeliverybackend.entity.OrderStatus;

public class OrderNotReadyForDeliveryException
        extends RuntimeException {

    public OrderNotReadyForDeliveryException(
            OrderStatus currentStatus
    ) {
        super(
                "Order cannot be assigned for delivery while status is: "
                        + currentStatus
        );
    }
}