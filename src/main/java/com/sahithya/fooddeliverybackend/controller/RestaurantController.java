package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.CreateMenuCategoryRequest;
import com.sahithya.fooddeliverybackend.dto.request.RegisterRestaurantRequest;
import com.sahithya.fooddeliverybackend.dto.response.MenuCategoryResponse;
import com.sahithya.fooddeliverybackend.dto.response.RegisterRestaurantResponse;
import com.sahithya.fooddeliverybackend.dto.response.RestaurantMenuResponse;
import com.sahithya.fooddeliverybackend.service.MenuCategoryService;
import com.sahithya.fooddeliverybackend.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MenuCategoryService menuCategoryService;

    public RestaurantController(
            RestaurantService restaurantService,
            MenuCategoryService menuCategoryService
    ) {
        this.restaurantService = restaurantService;
        this.menuCategoryService = menuCategoryService;
    }

    @PostMapping
    public ResponseEntity<RegisterRestaurantResponse> register(
            @Valid @RequestBody RegisterRestaurantRequest request
    ) {
        RegisterRestaurantResponse response =
                restaurantService.register(request);

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
}