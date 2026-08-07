package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.AddToCartRequest;
import com.sahithya.fooddeliverybackend.dto.request.UpdateCartItemRequest;
import com.sahithya.fooddeliverybackend.dto.response.AddToCartResponse;
import com.sahithya.fooddeliverybackend.dto.response.CartSummaryResponse;
import com.sahithya.fooddeliverybackend.entity.Cart;
import com.sahithya.fooddeliverybackend.entity.CartItem;
import com.sahithya.fooddeliverybackend.entity.CartStatus;
import com.sahithya.fooddeliverybackend.entity.MenuItem;
import com.sahithya.fooddeliverybackend.entity.Restaurant;
import com.sahithya.fooddeliverybackend.entity.User;
import com.sahithya.fooddeliverybackend.exception.*;
import com.sahithya.fooddeliverybackend.mapper.CartMapper;
import com.sahithya.fooddeliverybackend.repository.CartItemRepository;
import com.sahithya.fooddeliverybackend.repository.CartRepository;
import com.sahithya.fooddeliverybackend.repository.MenuItemRepository;
import com.sahithya.fooddeliverybackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CartMapper cartMapper,
            UserRepository userRepository,
            MenuItemRepository menuItemRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public AddToCartResponse addToCart(
            UUID userId,
            AddToCartRequest request
    ) {
        Instant now = Instant.now();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UUID menuItemId = request.getMenuItemId();

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(
                        () -> new MenuItemNotFoundException(menuItemId)
                );

        if (menuItem.isDeleted() || !menuItem.isAvailable()) {
            throw new MenuItemNotAvailableException(menuItemId);
        }

        Restaurant requestedRestaurant =
                menuItem.getCategory().getRestaurant();

        Optional<Cart> existingCart =
                cartRepository.findByUserIdAndCartStatus(
                        userId,
                        CartStatus.ACTIVE
                );

        Cart cart;

        if (existingCart.isPresent()) {
            cart = existingCart.get();

            /*
             * An empty cart may have no restaurant assigned.
             */
            if (cart.getRestaurant() == null) {
                cart.assignRestaurant(requestedRestaurant, now);
            } else if (!cart.getRestaurant()
                    .getId()
                    .equals(requestedRestaurant.getId())) {

                throw new DifferentRestaurantCartException();
            }
        } else {
            Cart newCart = cartMapper.toCartEntity(
                    user,
                    requestedRestaurant,
                    now
            );

            cart = cartRepository.save(newCart);
        }

        Optional<CartItem> existingCartItem =
                cartItemRepository.findByCartIdAndMenuItemId(
                        cart.getId(),
                        menuItemId
                );

        CartItem finalCartItem;

        if (existingCartItem.isPresent()) {
            finalCartItem = existingCartItem.get();
            finalCartItem.increaseQuantity(request.getQuantity());

            // No explicit save needed: managed entity + @Transactional
        } else {
            CartItem newCartItem = cartMapper.toCartItemEntity(
                    cart,
                    menuItem,
                    request.getQuantity(),
                    now
            );

            finalCartItem = cartItemRepository.save(newCartItem);
        }

        return cartMapper.toAddToCartResponse(
                cart,
                finalCartItem
        );
    }

    @Transactional(readOnly = true)
    public CartSummaryResponse getCartSummary(UUID userId) {

        Optional<Cart> optionalCart =
                cartRepository.findByUserIdAndCartStatus(
                        userId,
                        CartStatus.ACTIVE
                );

        if (optionalCart.isEmpty()) {
            return cartMapper.emptyCartSummary();
        }

        Cart cart = optionalCart.get();

        List<CartItem> cartItems =
                cartItemRepository.findAllByCartIdWithMenuItem(
                        cart.getId()
                );

        return cartMapper.toCartSummaryResponse(
                cart,
                cartItems
        );
    }

    @Transactional
    public AddToCartResponse updateCartItem(
            UUID userId,
            UUID cartItemId,
            UpdateCartItemRequest request
    ) {
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(
                        () -> new CartItemNotFoundException(cartItemId)
                );

        UUID cartOwnerId =
                cartItem.getCart().getUser().getId();

        if (!cartOwnerId.equals(userId)) {
            throw new CartItemNotFoundException(cartItemId);
        }

        cartItem.updateQuantity(
                request.getQuantity(),
                Instant.now()
        );

        return cartMapper.toAddToCartResponse(
                cartItem.getCart(),
                cartItem
        );
    }

    @Transactional
    public void deleteCartItem(
            UUID userId,
            UUID cartItemId
    ) {
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(
                        () -> new CartItemNotFoundException(cartItemId)
                );

        Cart cart = cartItem.getCart();

        UUID cartOwnerId = cart.getUser().getId();

        if (!cartOwnerId.equals(userId)) {
            throw new CartItemNotFoundException(cartItemId);
        }

        cartItemRepository.delete(cartItem);

        /*
         * delete() may be queued until flush/commit.
         * Flush now so the following count sees the deletion.
         */
        cartItemRepository.flush();

        long remainingItems =
                cartItemRepository.countByCartId(cart.getId());

        if (remainingItems == 0) {
            cart.clearRestaurant(Instant.now());
        }
    }
}