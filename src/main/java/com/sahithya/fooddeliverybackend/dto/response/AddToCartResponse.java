package com.sahithya.fooddeliverybackend.dto.response;

import java.util.UUID;

public class AddToCartResponse {
    private final UUID cartId;

    private final UUID cartItemId;

    private final UUID menuItemId;

    private final Integer quantity;

    public AddToCartResponse(
            UUID cartId,
            UUID cartItemId,
            UUID menuItemId,
            Integer quantity
    ) {
        this.cartId = cartId;
        this.cartItemId = cartItemId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public UUID getCartId() {
        return cartId;
    }

    public UUID getCartItemId() {
        return cartItemId;
    }

    public UUID getMenuItemId() {
        return menuItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
