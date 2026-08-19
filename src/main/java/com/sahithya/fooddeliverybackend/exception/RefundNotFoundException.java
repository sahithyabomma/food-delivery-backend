package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class RefundNotFoundException extends RuntimeException {

    public RefundNotFoundException(UUID refundId) {
        super("Refund not found with id: " + refundId);
    }
}