package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.PaymentResultRequest;
import com.sahithya.fooddeliverybackend.dto.response.PaymentResponse;
import com.sahithya.fooddeliverybackend.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentResultController {

    private final PaymentService paymentService;

    public PaymentResultController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{paymentId}/result")
    public ResponseEntity<PaymentResponse> updatePaymentResult(
            @PathVariable UUID paymentId,

            @RequestHeader("X-Payment-Timestamp")
            String timestamp,

            @RequestHeader("X-Payment-Signature")
            String signature,

            @Valid @RequestBody
            PaymentResultRequest request
    ) {

        PaymentResponse response =
                paymentService.updatePaymentResult(
                        paymentId,
                        timestamp,
                        signature,
                        request
                );

        return ResponseEntity.ok(response);
    }
}
