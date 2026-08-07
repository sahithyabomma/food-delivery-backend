package com.sahithya.fooddeliverybackend.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemSummaryResponse {

    private final UUID cartItemId;
    private final UUID menuItemId;
    private final String name;
    private final Integer quantity;
    private final BigDecimal price;
    private final BigDecimal subtotal;

    public CartItemSummaryResponse(
            UUID cartItemId,
            UUID menuItemId,
            String name,
            Integer quantity,
            BigDecimal price,
            BigDecimal subtotal
    ) {
        this.cartItemId = cartItemId;
        this.menuItemId = menuItemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    public UUID getCartItemId() {
        return cartItemId;
    }

    public UUID getMenuItemId() {
        return menuItemId;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}