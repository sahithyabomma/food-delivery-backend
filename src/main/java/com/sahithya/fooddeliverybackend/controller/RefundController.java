package com.sahithya.fooddeliverybackend.controller;

import com.sahithya.fooddeliverybackend.dto.request.RefundResultRequest;
import com.sahithya.fooddeliverybackend.dto.response.RefundResponse;
import com.sahithya.fooddeliverybackend.service.RefundService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(
            RefundService refundService
    ) {
        this.refundService = refundService;
    }

    @PostMapping("/{refundId}/result")
    public ResponseEntity<RefundResponse> updateRefundResult(
            @PathVariable UUID refundId,
            @Valid @RequestBody RefundResultRequest request
    ) {
        return ResponseEntity.ok(
                refundService.updateRefundResult(
                        refundId,
                        request
                )
        );
    }

    @GetMapping("/{orderId}/refund")
    public ResponseEntity<RefundResponse> getRefund(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID orderId
    ) {
        UUID userId =
                UUID.fromString(jwt.getSubject());

        RefundResponse response =
                refundService.getRefundForOrder(
                        userId,
                        orderId
                );

        return ResponseEntity.ok(response);
    }
}
