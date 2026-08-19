package com.sahithya.fooddeliverybackend.exception;

public class InvalidPaymentSignatureException
        extends RuntimeException {

    public InvalidPaymentSignatureException() {
        super("Invalid payment callback signature");
    }
}