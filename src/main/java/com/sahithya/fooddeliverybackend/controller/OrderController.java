package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.response.CheckoutResponse;
import com.sahithya.fooddeliverybackend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        CheckoutResponse response =
                orderService.checkout(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}