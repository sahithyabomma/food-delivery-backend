package com.sahithya.fooddeliverybackend.exception;

public class DuplicateTransactionReferenceException
        extends RuntimeException {

    public DuplicateTransactionReferenceException(
            String transactionReference
    ) {
        super(
                "Transaction reference has already been processed: "
                        + transactionReference
        );
    }
}