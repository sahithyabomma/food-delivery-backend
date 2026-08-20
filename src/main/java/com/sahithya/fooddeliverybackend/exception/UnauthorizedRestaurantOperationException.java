package com.sahithya.fooddeliverybackend.exception;

public class UnauthorizedRestaurantOperationException
        extends RuntimeException {

    public UnauthorizedRestaurantOperationException() {
        super(
                "You are not authorized to perform this operation for this restaurant"
        );
    }
}