package com.sahithya.fooddeliverybackend.exception;

public class InventoryConcurrencyException extends RuntimeException {

    public static final String MESSAGE =
            "Inventory changed while the request was being processed. Please retry.";

    public InventoryConcurrencyException() {
        super(MESSAGE);
    }
}
