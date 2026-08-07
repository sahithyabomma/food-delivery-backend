package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.FoodType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class MenuItemResponse {

    private final UUID id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final FoodType foodType;
    private final boolean available;
    private final UUID categoryId;
    private final Instant createdAt;

    public MenuItemResponse(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            FoodType foodType,
            boolean available,
            UUID categoryId,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.foodType = foodType;
        this.available = available;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public boolean isAvailable() {
        return available;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}