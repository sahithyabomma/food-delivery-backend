package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.response.*;
import com.sahithya.fooddeliverybackend.entity.*;
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

    public OrderSummaryResponse toOrderSummaryResponse(
            Order order,
            OrderPaymentState orderPaymentState,
            RefundStatus refundStatus
    ) {
        return new OrderSummaryResponse(
                order.getId(),
                order.getRestaurant().getName(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                orderPaymentState,
                refundStatus
        );
    }

    public OrderDetailsResponse toOrderDetailsResponse(
            Order order,
            List<OrderItem> orderItems,
            OrderPaymentState paymentState,
            RefundSummaryResponse refund
    ) {
        OrderRestaurantSummaryResponse restaurantResponse =
                new OrderRestaurantSummaryResponse(
                        order.getRestaurant().getId(),
                        order.getRestaurant().getName()
                );

        List<OrderItemResponse> itemResponses =
                orderItems.stream()
                        .map(this::toOrderItemResponse)
                        .toList();

        return new OrderDetailsResponse(
                order.getId(),
                order.getStatus(),
                restaurantResponse,

                itemResponses,

                order.getTotalAmount(),

                order.getCreatedAt(),
                paymentState,
                refund
        );
    }

    public UpdateOrderStatusResponse toUpdateStatusResponse(
            Order order
    ) {
        return new UpdateOrderStatusResponse(
                order.getId(),
                order.getStatus(),
                order.getUpdatedAt()
        );
    }
}