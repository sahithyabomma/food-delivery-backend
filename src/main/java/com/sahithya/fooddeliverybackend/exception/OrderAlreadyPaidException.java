package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class OrderAlreadyPaidException extends RuntimeException {

    public OrderAlreadyPaidException(UUID orderId) {
        super("Order has already been paid: " + orderId);
    }
}