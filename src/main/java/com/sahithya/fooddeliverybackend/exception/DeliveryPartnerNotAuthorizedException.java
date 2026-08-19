package com.sahithya.fooddeliverybackend.exception;

public class DeliveryPartnerNotAuthorizedException
        extends RuntimeException {

    public DeliveryPartnerNotAuthorizedException() {
        super(
                "You are not authorized to update this delivery"
        );
    }
}