package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class OrderPaymentRequiredException
        extends RuntimeException {

    public OrderPaymentRequiredException(UUID orderId) {
        super(
                "Order must be paid before it can be confirmed: "
                        + orderId
        );
    }
}