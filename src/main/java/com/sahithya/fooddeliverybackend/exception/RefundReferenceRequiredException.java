package com.sahithya.fooddeliverybackend.exception;

public class RefundReferenceRequiredException
        extends RuntimeException {

    public RefundReferenceRequiredException() {
        super(
                "Gateway refund reference is required for a successful refund"
        );
    }
}
