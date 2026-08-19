package com.sahithya.fooddeliverybackend.dto.request;

import com.sahithya.fooddeliverybackend.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class CreatePaymentRequest {

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    public CreatePaymentRequest() {
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
            PaymentMethod paymentMethod
    ) {
        this.paymentMethod = paymentMethod;
    }
}