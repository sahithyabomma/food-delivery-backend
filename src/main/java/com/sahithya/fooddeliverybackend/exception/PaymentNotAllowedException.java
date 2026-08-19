package com.sahithya.fooddeliverybackend.exception;

public class PaymentNotAllowedException
        extends RuntimeException {

    public PaymentNotAllowedException(String message) {
        super(message);
    }
}