package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.request.CreateMenuCategoryRequest;
import com.sahithya.fooddeliverybackend.dto.response.MenuCategoryResponse;
import com.sahithya.fooddeliverybackend.dto.response.MenuCategoryWithItemsResponse;
import com.sahithya.fooddeliverybackend.dto.response.MenuItemSummaryResponse;
import com.sahithya.fooddeliverybackend.entity.MenuCategory;
import com.sahithya.fooddeliverybackend.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class MenuCategoryMapper {

    public MenuCategory toEntity(
            CreateMenuCategoryRequest request,
            Restaurant restaurant,
            Instant now
    ) {
        return new MenuCategory(
                UUID.randomUUID(),
                request.getName().trim(),
                restaurant,
                now,
                now
        );
    }

    public MenuCategoryResponse toResponse(
            MenuCategory category
    ) {
        return new MenuCategoryResponse(
                category.getId(),
                category.getName(),
                category.getRestaurant().getId(),
                category.getCreatedAt()
        );
    }

    public MenuCategoryWithItemsResponse toWithItemsResponse(
            MenuCategory category,
            List<MenuItemSummaryResponse> items
    ) {
        return new MenuCategoryWithItemsResponse(
                category.getId(),
                category.getName(),
                items
        );
    }
}
