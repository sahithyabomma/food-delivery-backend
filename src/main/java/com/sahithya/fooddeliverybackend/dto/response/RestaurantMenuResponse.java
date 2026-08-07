package com.sahithya.fooddeliverybackend.dto.response;

import java.util.List;
import java.util.UUID;

public class RestaurantMenuResponse {

    private final UUID restaurantId;
    private final String restaurantName;
    private final List<MenuCategoryWithItemsResponse> categories;

    public RestaurantMenuResponse(
            UUID restaurantId,
            String restaurantName,
            List<MenuCategoryWithItemsResponse> categories
    ) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.categories = List.copyOf(categories);
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public List<MenuCategoryWithItemsResponse> getCategories() {
        return categories;
    }
}
