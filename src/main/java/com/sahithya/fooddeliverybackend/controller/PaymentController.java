package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.CreatePaymentRequest;
import com.sahithya.fooddeliverybackend.dto.request.PaymentResultRequest;
import com.sahithya.fooddeliverybackend.dto.response.PaymentResponse;
import com.sahithya.fooddeliverybackend.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}/payments")
    public ResponseEntity<PaymentResponse> createPayment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId,
            @RequestHeader("Idempotency-Key")
            String idempotencyKey,
            @Valid @RequestBody
            CreatePaymentRequest request
    ) {
        UUID userId =
                UUID.fromString(jwt.getSubject());

        PaymentResponse response =
                paymentService.createPayment(
                        userId,
                        orderId,
                        idempotencyKey,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{orderId}/payments")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistory(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        List<PaymentResponse> response =
                paymentService.getPaymentHistory(
                        userId,
                        orderId
                );

        return ResponseEntity.ok(response);
    }
}