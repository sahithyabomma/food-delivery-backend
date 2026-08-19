package com.sahithya.fooddeliverybackend.dto.request;

import com.sahithya.fooddeliverybackend.entity.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentResultRequest {

    @NotBlank(message = "Gateway event id is required")
    private String eventId;

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    private String transactionReference;

    public PaymentResultRequest() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }
}