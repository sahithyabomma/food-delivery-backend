package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.AddToCartRequest;
import com.sahithya.fooddeliverybackend.dto.request.UpdateCartItemRequest;
import com.sahithya.fooddeliverybackend.dto.response.AddToCartResponse;
import com.sahithya.fooddeliverybackend.dto.response.CartSummaryResponse;
import com.sahithya.fooddeliverybackend.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/item")
    public ResponseEntity<AddToCartResponse> addToCart(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AddToCartRequest request
            ) {
        UUID userId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
        AddToCartResponse response = cartService.addToCart(userId, request);
                return ResponseEntity
                        .status(HttpStatus.CREATED).body(response);

    }

    @GetMapping
    public ResponseEntity<CartSummaryResponse> getCart(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        CartSummaryResponse response =
                cartService.getCartSummary(userId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<AddToCartResponse> updateCartItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        AddToCartResponse response =
                cartService.updateCartItem(
                        userId,
                        cartItemId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID cartItemId
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        cartService.deleteCartItem(userId, cartItemId);

        return ResponseEntity.noContent().build();
    }
}
