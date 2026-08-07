package com.sahithya.fooddeliverybackend.exception;

public class DifferentRestaurantCartException extends RuntimeException {

    public DifferentRestaurantCartException() {
        super("Your cart contains items from another restaurant. Clear the cart before adding items from a different restaurant.");
    }
}
