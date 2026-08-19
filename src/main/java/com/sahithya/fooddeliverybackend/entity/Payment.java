package com.sahithya.fooddeliverybackend.entity;

import com.sahithya.fooddeliverybackend.exception.InvalidPaymentStatusTransitionException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(
            name = "idempotency_key",
            nullable = false,
            unique = true
    )
    private String idempotencyKey;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(
            name = "transaction_reference",
            unique = true
    )
    private String transactionReference;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Payment() {
    }

    public Payment(
            UUID id,
            Order order,
            BigDecimal amount,
            PaymentStatus status,
            PaymentMethod paymentMethod,
            String transactionReference,
            String idempotencyKey,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.order = order;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.idempotencyKey = idempotencyKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public UUID getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }


    public void markSuccess(
            String transactionReference,
            Instant now
    ) {
        if (this.status == PaymentStatus.SUCCESS) {
            return;
        }

        if (this.status != PaymentStatus.PENDING) {
            throw new InvalidPaymentStatusTransitionException(
                    this.status,
                    PaymentStatus.SUCCESS
            );
        }

        this.status = PaymentStatus.SUCCESS;
        this.transactionReference = transactionReference;
        this.updatedAt = now;
    }

    public void markFailed(Instant now) {
        if (this.status == PaymentStatus.FAILED) {
            return;
        }

        if (this.status != PaymentStatus.PENDING) {
            throw new InvalidPaymentStatusTransitionException(
                    this.status,
                    PaymentStatus.FAILED
            );
        }

        this.status = PaymentStatus.FAILED;
        this.updatedAt = now;
    }
}