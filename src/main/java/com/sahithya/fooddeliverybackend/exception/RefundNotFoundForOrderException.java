package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class RefundNotFoundForOrderException
        extends RuntimeException {

    public RefundNotFoundForOrderException(UUID orderId) {
        super(
                "No refund found for order: " + orderId
        );
    }
}