package com.sahithya.fooddeliverybackend.exception;

public class InvalidRestaurantHoursException
        extends RuntimeException {

    public InvalidRestaurantHoursException(String message) {
        super(message);
    }
}