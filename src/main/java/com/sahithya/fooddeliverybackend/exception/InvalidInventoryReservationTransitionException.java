package com.sahithya.fooddeliverybackend.exception;

import com.sahithya.fooddeliverybackend.entity.InventoryReservationStatus;

public class InvalidInventoryReservationTransitionException
        extends RuntimeException {

    public InvalidInventoryReservationTransitionException(
            InventoryReservationStatus current,
            InventoryReservationStatus requested
    ) {
        super(
                "Cannot change inventory reservation status from "
                        + current
                        + " to "
                        + requested
        );
    }
}