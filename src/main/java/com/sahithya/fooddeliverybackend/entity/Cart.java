package com.sahithya.fooddeliverybackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "carts",
        indexes = @Index(
                name = "idx_cart_user_status",
                columnList = "user_id, status"
        )
)
public class Cart {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CartStatus cartStatus;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private Long version;

    protected Cart() {
    }

    public Cart(
            UUID id,
            User user,
            Restaurant restaurant,
            CartStatus cartStatus,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.cartStatus = cartStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public CartStatus getCartStatus() {
        return cartStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void clearRestaurant(Instant updatedAt) {
        this.restaurant = null;
        this.updatedAt = updatedAt;
    }

    public void assignRestaurant(
            Restaurant restaurant,
            Instant updatedAt
    ) {
        this.restaurant = restaurant;
        this.updatedAt = updatedAt;
    }

}
