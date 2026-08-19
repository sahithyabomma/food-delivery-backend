package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.CreateMenuCategoryRequest;
import com.sahithya.fooddeliverybackend.dto.request.RegisterRestaurantRequest;
import com.sahithya.fooddeliverybackend.dto.response.*;
import com.sahithya.fooddeliverybackend.service.MenuCategoryService;
import com.sahithya.fooddeliverybackend.service.OrderService;
import com.sahithya.fooddeliverybackend.service.RestaurantService;
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
@RequestMapping("/restaurants")
@Validated
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MenuCategoryService menuCategoryService;
    private final OrderService orderService;

    public RestaurantController(
            RestaurantService restaurantService,
            MenuCategoryService menuCategoryService, OrderService orderService
    ) {
        this.restaurantService = restaurantService;
        this.menuCategoryService = menuCategoryService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<RegisterRestaurantResponse> register(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RegisterRestaurantRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        RegisterRestaurantResponse response =
                restaurantService.register(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/{restaurantId}/categories")
    public ResponseEntity<MenuCategoryResponse> createCategory(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody CreateMenuCategoryRequest request
    ) {
        MenuCategoryResponse response =
                menuCategoryService.create(
                        restaurantId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<RestaurantMenuResponse> getMenu(
            @PathVariable UUID restaurantId
    ) {
        return ResponseEntity.ok(
                restaurantService.getMenu(restaurantId)
        );
    }

    @GetMapping("/{restaurantId}/orders")
    public ResponseEntity<PageResponse<OrderSummaryResponse>>
    getRestaurantOrders(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID restaurantId,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be 0 or greater")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size cannot exceed 100")
            int size
    ) {

        UUID ownerUserId =
                UUID.fromString(jwt.getSubject());

        PageResponse<OrderSummaryResponse> response =
                orderService.getRestaurantOrders(
                        ownerUserId,
                        restaurantId,
                        page,
                        size
                );

        return ResponseEntity.ok(response);
    }
}