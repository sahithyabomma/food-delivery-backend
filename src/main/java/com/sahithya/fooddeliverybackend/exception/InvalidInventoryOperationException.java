package com.sahithya.fooddeliverybackend.exception;

public class InvalidInventoryOperationException
        extends RuntimeException {

    public InvalidInventoryOperationException(String message) {
        super(message);
    }
}