package com.sahithya.fooddeliverybackend.dto.response;

import java.util.List;
import java.util.UUID;

public class MenuCategoryWithItemsResponse {

    private final UUID id;
    private final String name;
    private final List<MenuItemSummaryResponse> items;

    public MenuCategoryWithItemsResponse(
            UUID id,
            String name,
            List<MenuItemSummaryResponse> items
    ) {
        this.id = id;
        this.name = name;
        this.items = List.copyOf(items);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MenuItemSummaryResponse> getItems() {
        return items;
    }
}
