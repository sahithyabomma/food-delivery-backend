package com.sahithya.fooddeliverybackend.exception;

import com.sahithya.fooddeliverybackend.entity.PaymentStatus;

public class InvalidPaymentStatusTransitionException
        extends RuntimeException {

    public InvalidPaymentStatusTransitionException(
            PaymentStatus current,
            PaymentStatus requested
    ) {
        super(
                "Cannot change payment status from "
                        + current
                        + " to "
                        + requested
        );
    }
}