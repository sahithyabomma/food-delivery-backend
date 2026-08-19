package com.sahithya.fooddeliverybackend.exception;

public class PaymentTransactionReferenceRequiredException
        extends RuntimeException {

    public PaymentTransactionReferenceRequiredException() {
        super(
                "Transaction reference is required for a successful payment"
        );
    }
}