package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.MenuCategory;
import com.sahithya.fooddeliverybackend.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuCategoryRepository
        extends JpaRepository<MenuCategory, UUID> {

    boolean existsByRestaurantIdAndNameIgnoreCase(
            UUID restaurantId,
            String name
    );

    List<MenuCategory> findByRestaurantIdOrderByNameAsc(
            UUID restaurantId
    );
}