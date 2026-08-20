package com.sahithya.fooddeliverybackend.entity;

import com.sahithya.fooddeliverybackend.exception.InsufficientInventoryException;
import com.sahithya.fooddeliverybackend.exception.InvalidInventoryOperationException;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_inventory_menu_item",
                        columnNames = "menu_item_id"
                )
        }
)
public class Inventory {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "menu_item_id",
            nullable = false,
            unique = true
    )
    private MenuItem menuItem;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Inventory() {
    }

    public Inventory(
            UUID id,
            MenuItem menuItem,
            int availableQuantity,
            int reservedQuantity,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.menuItem = menuItem;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void reserve(
            int quantity,
            Instant now
    ) {
        validatePositiveQuantity(quantity);

        if (availableQuantity < quantity) {
            throw new InsufficientInventoryException(
                    menuItem.getId(),
                    quantity,
                    availableQuantity
            );
        }

        availableQuantity -= quantity;
        reservedQuantity += quantity;
        updatedAt = now;
    }

    public void release(
            int quantity,
            Instant now
    ) {
        validatePositiveQuantity(quantity);

        if (reservedQuantity < quantity) {
            throw new InvalidInventoryOperationException(
                    "Cannot release more inventory than is currently reserved"
            );
        }

        reservedQuantity -= quantity;
        availableQuantity += quantity;
        updatedAt = now;
    }

    public void confirm(
            int quantity,
            Instant now
    ) {
        validatePositiveQuantity(quantity);

        if (reservedQuantity < quantity) {
            throw new InvalidInventoryOperationException(
                    "Cannot confirm more inventory than is currently reserved"
            );
        }

        reservedQuantity -= quantity;
        updatedAt = now;
    }

    public void restock(
            int quantity,
            Instant now
    ) {
        validatePositiveQuantity(quantity);

        availableQuantity += quantity;
        updatedAt = now;
    }

    private void validatePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidInventoryOperationException(
                    "Inventory quantity must be greater than zero"
            );
        }
    }

    public UUID getId() {
        return id;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}