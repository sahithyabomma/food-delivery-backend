package com.sahithya.fooddeliverybackend.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemResponse {

    private final UUID menuItemId;
    private final String menuItemName;
    private final BigDecimal priceAtPurchase;
    private final Integer quantity;
    private final BigDecimal subtotal;

    public OrderItemResponse(
            UUID menuItemId,
            String menuItemName,
            BigDecimal priceAtPurchase,
            Integer quantity,
            BigDecimal subtotal
    ) {
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.priceAtPurchase = priceAtPurchase;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public UUID getMenuItemId() {
        return menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}