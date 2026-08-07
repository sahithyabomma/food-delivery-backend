package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.response.AddToCartResponse;
import com.sahithya.fooddeliverybackend.dto.response.CartItemSummaryResponse;
import com.sahithya.fooddeliverybackend.dto.response.CartRestaurantSummaryResponse;
import com.sahithya.fooddeliverybackend.dto.response.CartSummaryResponse;
import com.sahithya.fooddeliverybackend.entity.Cart;
import com.sahithya.fooddeliverybackend.entity.CartItem;
import com.sahithya.fooddeliverybackend.entity.CartStatus;
import com.sahithya.fooddeliverybackend.entity.MenuItem;
import com.sahithya.fooddeliverybackend.entity.Restaurant;
import com.sahithya.fooddeliverybackend.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class CartMapper {

    public Cart toCartEntity(
            User user,
            Restaurant restaurant,
            Instant now
    ) {
        return new Cart(
                UUID.randomUUID(),
                user,
                restaurant,
                CartStatus.ACTIVE,
                now,
                now
        );
    }

    public CartItem toCartItemEntity(
            Cart cart,
            MenuItem menuItem,
            Integer quantity,
            Instant now
    ) {
        return new CartItem(
                UUID.randomUUID(),
                cart,
                menuItem,
                quantity,
                now,
                now
        );
    }

    public AddToCartResponse toAddToCartResponse(
            Cart cart,
            CartItem cartItem
    ) {
        return new AddToCartResponse(
                cart.getId(),
                cartItem.getId(),
                cartItem.getMenuItem().getId(),
                cartItem.getQuantity()
        );
    }

    public CartSummaryResponse toCartSummaryResponse(
            Cart cart,
            List<CartItem> cartItems
    ) {
        if (cart == null) {
            return emptyCartSummary();
        }

        CartRestaurantSummaryResponse restaurantResponse = null;

        if (cart.getRestaurant() != null) {
            restaurantResponse = new CartRestaurantSummaryResponse(
                    cart.getRestaurant().getId(),
                    cart.getRestaurant().getName()
            );
        }

        List<CartItemSummaryResponse> itemResponses =
                cartItems.stream()
                        .map(this::toCartItemSummaryResponse)
                        .toList();

        BigDecimal total = itemResponses.stream()
                .map(CartItemSummaryResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartSummaryResponse(
                cart.getId(),
                restaurantResponse,
                itemResponses,
                total
        );
    }

    public CartSummaryResponse emptyCartSummary() {
        return new CartSummaryResponse(
                null,
                null,
                List.of(),
                BigDecimal.ZERO
        );
    }

    private CartItemSummaryResponse toCartItemSummaryResponse(
            CartItem cartItem
    ) {
        MenuItem currentMenuItem = cartItem.getMenuItem();

        BigDecimal currentPrice = currentMenuItem.getPrice();

        BigDecimal subtotal = currentPrice.multiply(
                BigDecimal.valueOf(cartItem.getQuantity())
        );

        return new CartItemSummaryResponse(
                cartItem.getId(),
                currentMenuItem.getId(),
                currentMenuItem.getName(),
                cartItem.getQuantity(),
                currentPrice,
                subtotal
        );
    }
}