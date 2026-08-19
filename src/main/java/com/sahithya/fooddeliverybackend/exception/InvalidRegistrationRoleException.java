package com.sahithya.fooddeliverybackend.exception;

import com.sahithya.fooddeliverybackend.entity.UserRole;

public class InvalidRegistrationRoleException extends RuntimeException {

    public InvalidRegistrationRoleException(UserRole role) {
        super(
                "Registration is not allowed with role: " + role
                        + ". Allowed roles are CUSTOMER, "
                        + "RESTAURANT_OWNER and DELIVERY_PARTNER."
        );
    }
}