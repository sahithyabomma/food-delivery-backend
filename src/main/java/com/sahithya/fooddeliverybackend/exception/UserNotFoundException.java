package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(final UUID id) {
        super("404 user " + id + " not found");
    }

}
