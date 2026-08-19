package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.request.CreatePaymentRequest;
import com.sahithya.fooddeliverybackend.dto.response.PaymentResponse;
import com.sahithya.fooddeliverybackend.entity.Order;
import com.sahithya.fooddeliverybackend.entity.Payment;
import com.sahithya.fooddeliverybackend.entity.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class PaymentMapper {

    public Payment toEntity(
            Order order,
            CreatePaymentRequest request,
            String idempotencyKey,
            Instant now
    ) {
        return new Payment(
                UUID.randomUUID(),
                order,
                order.getTotalAmount(),
                PaymentStatus.PENDING,
                request.getPaymentMethod(),
                null,
                idempotencyKey,
                now,
                now
        );
    }

    public PaymentResponse toResponse(
            Payment payment
    ) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionReference(),
                payment.getCreatedAt()
        );
    }
}