package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository
        extends JpaRepository<Inventory, UUID> {

    Optional<Inventory> findByMenuItemId(UUID menuItemId);

    boolean existsByMenuItemId(UUID menuItemId);

    List<Inventory> findByMenuItemIdIn(
            List<UUID> menuItemIds
    );
}
