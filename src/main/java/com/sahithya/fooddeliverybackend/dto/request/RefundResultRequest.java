package com.sahithya.fooddeliverybackend.dto.request;

import com.sahithya.fooddeliverybackend.entity.RefundStatus;
import jakarta.validation.constraints.NotNull;

public class RefundResultRequest {

    @NotNull(message = "Refund status is required")
    private RefundStatus status;

    private String gatewayRefundReference;

    public RefundResultRequest() {
    }

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public String getGatewayRefundReference() {
        return gatewayRefundReference;
    }

    public void setGatewayRefundReference(
            String gatewayRefundReference
    ) {
        this.gatewayRefundReference = gatewayRefundReference;
    }
}