package com.sahithya.fooddeliverybackend.exception;

public class RestaurantEmailAlreadyExistsException
        extends RuntimeException {

    public RestaurantEmailAlreadyExistsException(String email) {
        super("A restaurant with email '" + email + "' already exists");
    }
}