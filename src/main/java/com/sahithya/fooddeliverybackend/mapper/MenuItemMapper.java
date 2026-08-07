package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.request.CreateMenuItemRequest;
import com.sahithya.fooddeliverybackend.dto.response.MenuItemResponse;
import com.sahithya.fooddeliverybackend.dto.response.MenuItemSummaryResponse;
import com.sahithya.fooddeliverybackend.entity.MenuCategory;
import com.sahithya.fooddeliverybackend.entity.MenuItem;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class MenuItemMapper {

    public MenuItem toEntity(
            CreateMenuItemRequest request,
            MenuCategory category,
            Instant now
    ) {
        String normalizedDescription =
                request.getDescription() == null
                        ? null
                        : request.getDescription().trim();

        return new MenuItem(
                UUID.randomUUID(),
                request.getName().trim(),
                normalizedDescription,
                request.getPrice(),
                request.getFoodType(),
                true,
                false,
                null,
                category,
                now,
                now
        );
    }

    public MenuItemResponse toResponse(MenuItem item) {
        return new MenuItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getFoodType(),
                item.isAvailable(),
                item.getCategory().getId(),
                item.getCreatedAt()
        );
    }

    public MenuItemSummaryResponse toSummaryResponse(MenuItem item) {
        return new MenuItemSummaryResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getFoodType()
        );
    }
}
