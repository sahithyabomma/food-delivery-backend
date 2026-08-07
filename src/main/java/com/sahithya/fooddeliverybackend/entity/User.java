package com.sahithya.fooddeliverybackend.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    /**
     * Required by JPA/Hibernate.
     * Hibernate uses this constructor to create the entity
     * before populating its fields.
     */
    protected User() {
    }

    public User(UUID uuid,
                String name,
                String email,
                String passwordHash,
                UserRole userRole,
                Instant createdAt,
                Instant updatedAt) {
        this.id = uuid;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userRole = userRole;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return userRole;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void updateName(String name, Instant updatedAt) {
        this.name = name;
        this.updatedAt = updatedAt;
    }
}
