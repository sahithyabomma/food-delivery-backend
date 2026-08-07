package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.response.CheckoutResponse;
import com.sahithya.fooddeliverybackend.dto.response.OrderItemResponse;
import com.sahithya.fooddeliverybackend.dto.response.OrderRestaurantSummaryResponse;
import com.sahithya.fooddeliverybackend.entity.CartItem;
import com.sahithya.fooddeliverybackend.entity.MenuItem;
import com.sahithya.fooddeliverybackend.entity.Order;
import com.sahithya.fooddeliverybackend.entity.OrderItem;
import com.sahithya.fooddeliverybackend.entity.OrderStatus;
import com.sahithya.fooddeliverybackend.entity.Restaurant;
import com.sahithya.fooddeliverybackend.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class OrderMapper {

    public Order toOrderEntity(
            User user,
            Restaurant restaurant,
            BigDecimal totalAmount,
            Instant now
    ) {
        return new Order(
                UUID.randomUUID(),
                user,
                restaurant,
                OrderStatus.PLACED,
                totalAmount,
                now,
                now
        );
    }

    public OrderItem toOrderItemEntity(
            CartItem cartItem,
            Instant now
    ) {
        MenuItem menuItem = cartItem.getMenuItem();

        BigDecimal priceAtPurchase =
                menuItem.getPrice();

        BigDecimal subtotal =
                priceAtPurchase.multiply(
                        BigDecimal.valueOf(
                                cartItem.getQuantity()
                        )
                );

        return new OrderItem(
                UUID.randomUUID(),
                menuItem.getId(),
                menuItem.getName(),
                priceAtPurchase,
                cartItem.getQuantity(),
                subtotal,
                now
        );
    }

    public CheckoutResponse toCheckoutResponse(
            Order order
    ) {
        OrderRestaurantSummaryResponse restaurantResponse =
                new OrderRestaurantSummaryResponse(
                        order.getRestaurant().getId(),
                        order.getRestaurant().getName()
                );

        List<OrderItemResponse> itemResponses =
                order.getOrderItems()
                        .stream()
                        .map(this::toOrderItemResponse)
                        .toList();

        return new CheckoutResponse(
                order.getId(),
                order.getStatus(),
                restaurantResponse,
                itemResponses,
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }

    private OrderItemResponse toOrderItemResponse(
            OrderItem orderItem
    ) {
        return new OrderItemResponse(
                orderItem.getMenuItemId(),
                orderItem.getMenuItemName(),
                orderItem.getPriceAtPurchase(),
                orderItem.getQuantity(),
                orderItem.getSubtotal()
        );
    }
}