package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.UserRole;

import java.time.Instant;
import java.util.UUID;

public class RegisterUserResponse {
    private UUID id;
    private String email;
    private String name;
    private Instant createdAt;
    private UserRole role;

    public RegisterUserResponse(
            final  UUID id,
            final String email,
            final String name,
            final Instant createdAt,
            final UserRole role
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
