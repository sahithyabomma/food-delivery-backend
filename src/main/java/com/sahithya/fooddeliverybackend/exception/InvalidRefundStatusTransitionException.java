package com.sahithya.fooddeliverybackend.exception;

import com.sahithya.fooddeliverybackend.entity.RefundStatus;

public class InvalidRefundStatusTransitionException
        extends RuntimeException {

    public InvalidRefundStatusTransitionException(
            RefundStatus current,
            RefundStatus requested
    ) {
        super(
                "Cannot change refund status from "
                        + current
                        + " to "
                        + requested
        );
    }
}