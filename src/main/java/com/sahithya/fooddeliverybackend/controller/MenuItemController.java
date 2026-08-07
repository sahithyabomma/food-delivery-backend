package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.CreateMenuItemRequest;
import com.sahithya.fooddeliverybackend.dto.response.MenuItemResponse;
import com.sahithya.fooddeliverybackend.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(
            MenuItemService menuItemService
    ) {
        this.menuItemService = menuItemService;
    }

    @PostMapping("/{categoryId}/items")
    public ResponseEntity<MenuItemResponse> createItem(
            @PathVariable UUID categoryId,
            @Valid @RequestBody CreateMenuItemRequest request
    ) {
        MenuItemResponse response =
                menuItemService.create(categoryId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}