package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuItemRepository
        extends JpaRepository<MenuItem, UUID> {

    boolean existsByCategoryRestaurantIdAndNameIgnoreCase(
            UUID restaurantId,
            String name
    );

    List<MenuItem> findByCategoryRestaurantIdAndDeletedFalseOrderByNameAsc(
            UUID restaurantId
    );


}
