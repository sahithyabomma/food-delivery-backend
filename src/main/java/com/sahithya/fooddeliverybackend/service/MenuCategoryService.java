package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.CreateMenuCategoryRequest;
import com.sahithya.fooddeliverybackend.dto.response.MenuCategoryResponse;
import com.sahithya.fooddeliverybackend.entity.MenuCategory;
import com.sahithya.fooddeliverybackend.entity.Restaurant;
import com.sahithya.fooddeliverybackend.exception.MenuCategoryAlreadyExistsException;
import com.sahithya.fooddeliverybackend.exception.RestaurantNotFoundException;
import com.sahithya.fooddeliverybackend.mapper.MenuCategoryMapper;
import com.sahithya.fooddeliverybackend.repository.MenuCategoryRepository;
import com.sahithya.fooddeliverybackend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class MenuCategoryService {

    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuCategoryMapper menuCategoryMapper;

    public MenuCategoryService(
            RestaurantRepository restaurantRepository,
            MenuCategoryRepository menuCategoryRepository,
            MenuCategoryMapper menuCategoryMapper
    ) {
        this.restaurantRepository = restaurantRepository;
        this.menuCategoryRepository = menuCategoryRepository;
        this.menuCategoryMapper = menuCategoryMapper;
    }

    @Transactional
    public MenuCategoryResponse create(
            UUID restaurantId,
            CreateMenuCategoryRequest request
    ) {
        Restaurant restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(
                        () -> new RestaurantNotFoundException(
                                restaurantId
                        )
                );

        String normalizedName = request.getName().trim();

        boolean categoryExists =
                menuCategoryRepository
                        .existsByRestaurantIdAndNameIgnoreCase(
                                restaurantId,
                                normalizedName
                        );

        if (categoryExists) {
            throw new MenuCategoryAlreadyExistsException(
                    normalizedName
            );
        }

        Instant now = Instant.now();

        MenuCategory category = menuCategoryMapper.toEntity(
                request,
                restaurant,
                now
        );

        MenuCategory savedCategory =
                menuCategoryRepository.save(category);

        return menuCategoryMapper.toResponse(savedCategory);
    }
}