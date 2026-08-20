package com.sahithya.fooddeliverybackend.entity;

import com.sahithya.fooddeliverybackend.exception.InvalidInventoryReservationTransitionException;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "inventory_reservations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_inventory_reservation_order_item",
                        columnNames = {
                                "order_id",
                                "menu_item_id"
                        }
                )
        }
)
public class InventoryReservation {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "menu_item_id",
            nullable = false
    )
    private MenuItem menuItem;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryReservationStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected InventoryReservation() {
    }

    public InventoryReservation(
            UUID id,
            Order order,
            MenuItem menuItem,
            int quantity,
            InventoryReservationStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void confirm(Instant now) {

        if (status == InventoryReservationStatus.CONFIRMED) {
            return;
        }

        if (status != InventoryReservationStatus.RESERVED) {
            throw new InvalidInventoryReservationTransitionException(
                    status,
                    InventoryReservationStatus.CONFIRMED
            );
        }

        status = InventoryReservationStatus.CONFIRMED;
        updatedAt = now;
    }

    public void release(Instant now) {

        if (status == InventoryReservationStatus.RELEASED) {
            return;
        }

        if (status != InventoryReservationStatus.RESERVED) {
            throw new InvalidInventoryReservationTransitionException(
                    status,
                    InventoryReservationStatus.RELEASED
            );
        }

        status = InventoryReservationStatus.RELEASED;
        updatedAt = now;
    }

    public UUID getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public InventoryReservationStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
