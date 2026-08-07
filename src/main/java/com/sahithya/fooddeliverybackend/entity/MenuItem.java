package com.sahithya.fooddeliverybackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_type", nullable = false)
    private FoodType foodType;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private MenuCategory category;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected MenuItem() {
    }

    public MenuItem(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            FoodType foodType,
            boolean available,
            boolean deleted,
            Instant deletedAt,
            MenuCategory category,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.foodType = foodType;
        this.available = available;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void markUnavailable(Instant updatedAt) {
        this.available = false;
        this.updatedAt = updatedAt;
    }

    public void markAvailable(Instant updatedAt) {
        this.available = true;
        this.updatedAt = updatedAt;
    }

    public void softDelete(Instant deletedAt) {
        this.deleted = true;
        this.available = false;
        this.deletedAt = deletedAt;
        this.updatedAt = deletedAt;
    }
}