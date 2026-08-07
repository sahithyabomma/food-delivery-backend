package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository
        extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByCartIdAndMenuItemId(
            UUID cartId,
            UUID menuItemId
    );

    @Query("""
            SELECT ci
            FROM CartItem ci
            JOIN FETCH ci.menuItem mi
            WHERE ci.cart.id = :cartId
            ORDER BY mi.name ASC
            """)
    List<CartItem> findAllByCartIdWithMenuItem(
            @Param("cartId") UUID cartId
    );

    long countByCartId(UUID cartId);
}