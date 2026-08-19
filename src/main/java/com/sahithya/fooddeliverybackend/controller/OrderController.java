package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.UpdateOrderStatusRequest;
import com.sahithya.fooddeliverybackend.dto.response.*;
import com.sahithya.fooddeliverybackend.entity.UserRole;
import com.sahithya.fooddeliverybackend.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Validated
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

    @GetMapping
    public ResponseEntity<PageResponse<OrderSummaryResponse>> getOrderHistory(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be 0 or greater")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size cannot exceed 100")
            int size
    ) {
        UUID userId =
                UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
                orderService.getOrderHistory(
                        userId,
                        page,
                        size
                )
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        OrderDetailsResponse response =
                orderService.getOrderDetails(
                        userId,
                        orderId
                );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<UpdateOrderStatusResponse> updateOrderStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        UserRole role = UserRole.valueOf(
                jwt.getClaimAsString("role")
        );

        UpdateOrderStatusResponse response =
                orderService.updateOrderStatus(
                        userId,
                        role,
                        orderId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        orderService.cancelOrder(
                userId,
                orderId
        );

        return ResponseEntity.noContent().build();
    }
}