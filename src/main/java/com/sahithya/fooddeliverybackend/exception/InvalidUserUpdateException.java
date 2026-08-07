package com.sahithya.fooddeliverybackend.exception;

public class InvalidUserUpdateException extends RuntimeException {

    public InvalidUserUpdateException(String message) {
        super(message);
    }
}