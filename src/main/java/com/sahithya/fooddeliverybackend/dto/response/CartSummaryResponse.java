package com.sahithya.fooddeliverybackend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CartSummaryResponse {

    private final UUID cartId;
    private final CartRestaurantSummaryResponse restaurant;
    private final List<CartItemSummaryResponse> items;
    private final BigDecimal total;

    public CartSummaryResponse(
            UUID cartId,
            CartRestaurantSummaryResponse restaurant,
            List<CartItemSummaryResponse> items,
            BigDecimal total
    ) {
        this.cartId = cartId;
        this.restaurant = restaurant;
        this.items = items;
        this.total = total;
    }

    public UUID getCartId() {
        return cartId;
    }

    public CartRestaurantSummaryResponse getRestaurant() {
        return restaurant;
    }

    public List<CartItemSummaryResponse> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }
}