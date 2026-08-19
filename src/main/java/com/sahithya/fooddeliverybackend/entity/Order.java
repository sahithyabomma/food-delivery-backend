package com.sahithya.fooddeliverybackend.entity;

import com.sahithya.fooddeliverybackend.exception.InvalidOrderStatusTransitionException;
import com.sahithya.fooddeliverybackend.exception.OrderCancellationNotAllowedException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(
            name = "total_amount",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal totalAmount;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Order() {
    }

    public Order(
            UUID id,
            User user,
            Restaurant restaurant,
            OrderStatus status,
            BigDecimal totalAmount,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.assignOrder(this);
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

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void changeStatus(
            OrderStatus newStatus,
            Instant now
    ) {
        if (!isValidTransition(this.status, newStatus)) {
            throw new InvalidOrderStatusTransitionException(
                    this.status,
                    newStatus
            );
        }

        this.status = newStatus;
        this.updatedAt = now;
    }

    private boolean isValidTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus
    ) {
        return switch (currentStatus) {

            case PLACED ->
                    newStatus == OrderStatus.CONFIRMED
                            || newStatus == OrderStatus.CANCELLED;

            case CONFIRMED ->
                    newStatus == OrderStatus.PREPARING
                            || newStatus == OrderStatus.CANCELLED;

            case PREPARING ->
                    newStatus == OrderStatus.READY_FOR_PICKUP;

            case READY_FOR_PICKUP ->
                    newStatus == OrderStatus.OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY ->
                    newStatus == OrderStatus.DELIVERED;

            case DELIVERED, CANCELLED ->
                    false;
        };
    }

    public void cancel(Instant now) {

        boolean cancellable =
                this.status == OrderStatus.PLACED
                        || this.status == OrderStatus.CONFIRMED;

        if (!cancellable) {
            throw new OrderCancellationNotAllowedException(
                    this.status
            );
        }

        this.status = OrderStatus.CANCELLED;
        this.updatedAt = now;
    }
}