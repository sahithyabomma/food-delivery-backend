package com.sahithya.fooddeliverybackend.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "payment_webhook_events",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_payment_webhook_event_id",
                        columnNames = "event_id"
                )
        }
)
public class PaymentWebhookEvent {

    @Id
    private UUID id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    protected PaymentWebhookEvent() {
    }

    public PaymentWebhookEvent(
            UUID id,
            String eventId,
            Payment payment,
            Instant processedAt
    ) {
        this.id = id;
        this.eventId = eventId;
        this.payment = payment;
        this.processedAt = processedAt;
    }
}
