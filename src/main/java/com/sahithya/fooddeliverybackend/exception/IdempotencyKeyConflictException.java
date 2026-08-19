package com.sahithya.fooddeliverybackend.exception;

public class IdempotencyKeyConflictException
        extends RuntimeException {

    public IdempotencyKeyConflictException(
            String idempotencyKey
    ) {
        super(
                "Idempotency key has already been used for a different payment request: "
                        + idempotencyKey
        );
    }
}