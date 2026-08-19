package com.sahithya.fooddeliverybackend.entity;

import com.sahithya.fooddeliverybackend.exception.InvalidRefundStatusTransitionException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "refunds",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_refund_payment",
                        columnNames = "payment_id"
                )
        }
)
public class Refund {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "payment_id",
            nullable = false,
            unique = true
    )
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "gateway_refund_reference", unique = true)
    private String gatewayRefundReference;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Refund() {
    }

    public Refund(
            UUID id,
            Payment payment,
            Order order,
            BigDecimal amount,
            RefundStatus status,
            String reason,
            String gatewayRefundReference,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.payment = payment;
        this.order = order;
        this.amount = amount;
        this.status = status;
        this.reason = reason;
        this.gatewayRefundReference = gatewayRefundReference;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Payment getPayment() {
        return payment;
    }

    public Order getOrder() {
        return order;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getGatewayRefundReference() {
        return gatewayRefundReference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void markSuccess(
            String gatewayRefundReference,
            Instant now
    ) {
        if (this.status == RefundStatus.SUCCESS) {
            return;
        }

        if (this.status != RefundStatus.PENDING) {
            throw new InvalidRefundStatusTransitionException(
                    this.status,
                    RefundStatus.SUCCESS
            );
        }

        this.status = RefundStatus.SUCCESS;
        this.gatewayRefundReference = gatewayRefundReference;
        this.updatedAt = now;
    }

    public void markFailed(Instant now) {
        if (this.status == RefundStatus.FAILED) {
            return;
        }

        if (this.status != RefundStatus.PENDING) {
            throw new InvalidRefundStatusTransitionException(
                    this.status,
                    RefundStatus.FAILED
            );
        }

        this.status = RefundStatus.FAILED;
        this.updatedAt = now;
    }
}