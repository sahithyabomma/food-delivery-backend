package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.CreateMenuItemRequest;
import com.sahithya.fooddeliverybackend.dto.response.MenuItemResponse;
import com.sahithya.fooddeliverybackend.entity.MenuCategory;
import com.sahithya.fooddeliverybackend.entity.MenuItem;
import com.sahithya.fooddeliverybackend.exception.MenuCategoryNotFoundException;
import com.sahithya.fooddeliverybackend.exception.MenuItemAlreadyExistsException;
import com.sahithya.fooddeliverybackend.mapper.MenuItemMapper;
import com.sahithya.fooddeliverybackend.repository.MenuCategoryRepository;
import com.sahithya.fooddeliverybackend.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class MenuItemService {

    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemService(
            MenuCategoryRepository menuCategoryRepository,
            MenuItemRepository menuItemRepository,
            MenuItemMapper menuItemMapper
    ) {
        this.menuCategoryRepository = menuCategoryRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Transactional
    public MenuItemResponse create(
            UUID categoryId,
            CreateMenuItemRequest request
    ) {
        MenuCategory category = menuCategoryRepository
                .findById(categoryId)
                .orElseThrow(
                        () -> new MenuCategoryNotFoundException(categoryId)
                );

        String normalizedName = request.getName().trim();

        UUID restaurantId =
                category.getRestaurant().getId();

        boolean itemExists =
                menuItemRepository
                        .existsByCategoryRestaurantIdAndNameIgnoreCase(
                                restaurantId,
                                normalizedName
                        );

        if (itemExists) {
            throw new MenuItemAlreadyExistsException(normalizedName);
        }

        Instant now = Instant.now();

        MenuItem item = menuItemMapper.toEntity(
                request,
                category,
                now
        );

        MenuItem savedItem =
                menuItemRepository.save(item);

        return menuItemMapper.toResponse(savedItem);
    }
}