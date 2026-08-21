package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.FoodType;

import java.math.BigDecimal;
import java.util.UUID;

public class MenuItemSummaryResponse {

    private final UUID id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final FoodType foodType;
    private final boolean available;
    private final boolean inStock;

    public MenuItemSummaryResponse(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            FoodType foodType,
            boolean available,
            boolean inStock
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.foodType = foodType;
        this.available = available;
        this.inStock = inStock;
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

    public boolean isInStock() {
        return inStock;
    }
}
