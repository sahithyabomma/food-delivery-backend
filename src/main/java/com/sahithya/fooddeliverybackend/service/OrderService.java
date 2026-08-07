package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.response.CheckoutResponse;
import com.sahithya.fooddeliverybackend.entity.*;
import com.sahithya.fooddeliverybackend.exception.ActiveCartNotFoundException;
import com.sahithya.fooddeliverybackend.exception.EmptyCartException;
import com.sahithya.fooddeliverybackend.exception.MenuItemNotAvailableException;
import com.sahithya.fooddeliverybackend.mapper.OrderMapper;
import com.sahithya.fooddeliverybackend.repository.CartItemRepository;
import com.sahithya.fooddeliverybackend.repository.CartRepository;
import com.sahithya.fooddeliverybackend.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            OrderMapper orderMapper
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public CheckoutResponse checkout(UUID userId) {

        Instant now = Instant.now();

        Cart cart = cartRepository
                .findByUserIdAndCartStatus(
                        userId,
                        CartStatus.ACTIVE
                )
                .orElseThrow(ActiveCartNotFoundException::new);

        List<CartItem> cartItems =
                cartItemRepository.findAllByCartIdWithMenuItem(
                        cart.getId()
                );

        if (cartItems.isEmpty()) {
            throw new EmptyCartException();
        }

        for (CartItem cartItem : cartItems) {
            MenuItem menuItem = cartItem.getMenuItem();

            if (menuItem.isDeleted() || !menuItem.isAvailable()) {
                throw new MenuItemNotAvailableException(
                        menuItem.getId()
                );
            }
        }

        BigDecimal totalAmount = cartItems.stream()
                .map(cartItem -> {
                    MenuItem menuItem = cartItem.getMenuItem();

                    return menuItem.getPrice().multiply(
                            BigDecimal.valueOf(
                                    cartItem.getQuantity()
                            )
                    );
                })
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        Order order = orderMapper.toOrderEntity(
                cart.getUser(),
                cart.getRestaurant(),
                totalAmount,
                now
        );

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem =
                    orderMapper.toOrderItemEntity(
                            cartItem,
                            now
                    );

            order.addOrderItem(orderItem);
        }

        Order savedOrder =
                orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        cart.clearRestaurant(now);

        return orderMapper.toCheckoutResponse(savedOrder);
    }
}