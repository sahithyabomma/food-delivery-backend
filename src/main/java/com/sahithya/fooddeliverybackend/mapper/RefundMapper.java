package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.response.RefundResponse;
import com.sahithya.fooddeliverybackend.dto.response.RefundSummaryResponse;
import com.sahithya.fooddeliverybackend.entity.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class RefundMapper {

    public Refund toEntity(
            Payment payment,
            Order order,
            String reason,
            Instant now
    ) {
        return new Refund(
                UUID.randomUUID(),
                payment,
                order,
                payment.getAmount(),
                RefundStatus.PENDING,
                reason,
                null,
                now,
                now
        );
    }

    public RefundResponse toResponse(
            Refund refund
    ) {
        return new RefundResponse(
                refund.getId(),
                refund.getOrder().getId(),
                refund.getPayment().getId(),
                refund.getAmount(),
                refund.getStatus(),
                refund.getReason(),
                refund.getGatewayRefundReference(),
                refund.getCreatedAt()
        );
    }

    public RefundSummaryResponse toSummaryResponse(
            Refund refund
    ) {
        return new RefundSummaryResponse(
                refund.getId(),
                refund.getAmount(),
                refund.getStatus()
        );
    }
}