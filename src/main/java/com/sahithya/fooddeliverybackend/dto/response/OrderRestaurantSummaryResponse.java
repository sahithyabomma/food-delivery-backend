package com.sahithya.fooddeliverybackend.dto.response;

import java.util.UUID;

public class OrderRestaurantSummaryResponse {

    private final UUID id;
    private final String name;

    public OrderRestaurantSummaryResponse(
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