package com.sahithya.fooddeliverybackend.dto.response;

import java.util.UUID;

public class CartRestaurantSummaryResponse {

    private final UUID id;
    private final String name;

    public CartRestaurantSummaryResponse(
            UUID id,
            String name
    ) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}