package com.sahithya.fooddeliverybackend.entity;

import com.sahithya.fooddeliverybackend.exception.InvalidDeliveryStatusTransitionException;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

import static com.sahithya.fooddeliverybackend.entity.OrderStatus.DELIVERED;

@Entity
@Table(
        name = "delivery_assignments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_delivery_assignment_order",
                        columnNames = "order_id"
                )
        }
)
public class DeliveryAssignment {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            unique = true
    )
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "delivery_partner_id",
            nullable = false
    )
    private User deliveryPartner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    @Column(name = "picked_up_at")
    private Instant pickedUpAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    protected DeliveryAssignment() {
    }

    public DeliveryAssignment(
            UUID id,
            Order order,
            User deliveryPartner,
            DeliveryStatus status,
            Instant assignedAt
    ) {
        this.id = id;
        this.order = order;
        this.deliveryPartner = deliveryPartner;
        this.status = status;
        this.assignedAt = assignedAt;
    }

    public UUID getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public User getDeliveryPartner() {
        return deliveryPartner;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public Instant getPickedUpAt() {
        return pickedUpAt;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public void changeStatus(
            DeliveryStatus newStatus,
            Instant now
    ) {
        if (!isValidTransition(this.status, newStatus)) {
            throw new InvalidDeliveryStatusTransitionException(
                    this.status,
                    newStatus
            );
        }

        this.status = newStatus;

        if (newStatus == DeliveryStatus.PICKED_UP) {
            this.pickedUpAt = now;
        }

        if (newStatus == DeliveryStatus.DELIVERED) {
            this.deliveredAt = now;
        }
    }

    private boolean isValidTransition(
            DeliveryStatus current,
            DeliveryStatus next
    ) {
        return switch (current) {
            case ASSIGNED ->
                    next == DeliveryStatus.PICKED_UP;

            case PICKED_UP ->
                    next == DeliveryStatus.DELIVERED;

            case DELIVERED ->
                    false;
        };
    }
}