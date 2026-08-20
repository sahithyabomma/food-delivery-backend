package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.CreateInventoryRequest;
import com.sahithya.fooddeliverybackend.dto.request.RestockInventoryRequest;
import com.sahithya.fooddeliverybackend.dto.response.InventoryResponse;
import com.sahithya.fooddeliverybackend.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/menu-items")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService
    ) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/{menuItemId}/inventory")
    public ResponseEntity<InventoryResponse> createInventory(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID menuItemId,
            @Valid @RequestBody CreateInventoryRequest request
    ) {

        UUID ownerUserId =
                UUID.fromString(jwt.getSubject());

        InventoryResponse response =
                inventoryService.createInventory(
                        ownerUserId,
                        menuItemId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{menuItemId}/inventory/restock")
    public ResponseEntity<InventoryResponse> restock(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID menuItemId,
            @Valid @RequestBody RestockInventoryRequest request
    ) {
        UUID ownerUserId =
                UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok(
                inventoryService.restock(
                        ownerUserId,
                        menuItemId,
                        request.getQuantity()
                )
        );
    }
}
