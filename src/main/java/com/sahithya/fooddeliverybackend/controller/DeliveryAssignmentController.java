package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.AssignDeliveryPartnerRequest;
import com.sahithya.fooddeliverybackend.dto.request.UpdateDeliveryStatusRequest;
import com.sahithya.fooddeliverybackend.dto.response.DeliveryAssignmentResponse;
import com.sahithya.fooddeliverybackend.service.DeliveryAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class DeliveryAssignmentController {

    private final DeliveryAssignmentService deliveryAssignmentService;

    public DeliveryAssignmentController(
            DeliveryAssignmentService deliveryAssignmentService
    ) {
        this.deliveryAssignmentService =
                deliveryAssignmentService;
    }

    @PostMapping("/{orderId}/delivery-assignment")
    public ResponseEntity<DeliveryAssignmentResponse>
    assignDeliveryPartner(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId,
            @Valid @RequestBody
            AssignDeliveryPartnerRequest request
    ) {
        UUID restaurantOwnerUserId =
                UUID.fromString(jwt.getSubject());

        DeliveryAssignmentResponse response =
                deliveryAssignmentService
                        .assignDeliveryPartner(
                                restaurantOwnerUserId,
                                orderId,
                                request
                        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{orderId}/delivery-status")
    public ResponseEntity<DeliveryAssignmentResponse>
    updateDeliveryStatus(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId,
            @Valid @RequestBody
            UpdateDeliveryStatusRequest request
    ) {

        UUID deliveryPartnerUserId =
                UUID.fromString(jwt.getSubject());

        DeliveryAssignmentResponse response =
                deliveryAssignmentService
                        .updateDeliveryStatus(
                                deliveryPartnerUserId,
                                orderId,
                                request
                        );

        return ResponseEntity.ok(response);
    }
}