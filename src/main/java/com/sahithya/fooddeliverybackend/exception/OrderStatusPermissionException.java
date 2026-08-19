package com.sahithya.fooddeliverybackend.exception;

public class OrderStatusPermissionException extends RuntimeException {

    public OrderStatusPermissionException() {
        super("You are not allowed to perform this order status transition");
    }
}
