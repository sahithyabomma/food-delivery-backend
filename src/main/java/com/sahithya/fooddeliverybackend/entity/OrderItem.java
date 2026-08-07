package com.sahithya.fooddeliverybackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "menu_item_id", nullable = false)
    private UUID menuItemId;

    @Column(name = "menu_item_name", nullable = false)
    private String menuItemName;

    @Column(
            name = "price_at_purchase",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal priceAtPurchase;

    @Column(nullable = false)
    private Integer quantity;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal subtotal;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected OrderItem() {
    }

    public OrderItem(
            UUID id,
            UUID menuItemId,
            String menuItemName,
            BigDecimal priceAtPurchase,
            Integer quantity,
            BigDecimal subtotal,
            Instant createdAt
    ) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.priceAtPurchase = priceAtPurchase;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.createdAt = createdAt;
    }

    /**
     * Called only by Order.addOrderItem(...)
     */
    public void assignOrder(Order order) {
        this.order = order;
    }

    public UUID getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public UUID getMenuItemId() {
        return menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}