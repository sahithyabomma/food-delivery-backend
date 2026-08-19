package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class InvalidDeliveryPartnerException
        extends RuntimeException {

    public InvalidDeliveryPartnerException(UUID userId) {
        super(
                "User is not a valid delivery partner: "
                        + userId
        );
    }
}