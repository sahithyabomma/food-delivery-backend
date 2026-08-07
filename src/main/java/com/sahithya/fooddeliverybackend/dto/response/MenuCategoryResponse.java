package com.sahithya.fooddeliverybackend.dto.response;

import java.time.Instant;
import java.util.UUID;

public class MenuCategoryResponse {

    private final UUID id;
    private final String name;
    private final UUID restaurantId;
    private final Instant createdAt;

    public MenuCategoryResponse(
            UUID id,
            String name,
            UUID restaurantId,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.restaurantId = restaurantId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}