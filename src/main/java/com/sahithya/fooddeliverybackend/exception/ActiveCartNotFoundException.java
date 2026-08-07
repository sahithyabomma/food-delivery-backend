package com.sahithya.fooddeliverybackend.exception;

public class ActiveCartNotFoundException extends RuntimeException {

    public ActiveCartNotFoundException() {
        super("No active cart found for the user.");
    }
}