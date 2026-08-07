package com.sahithya.fooddeliverybackend.dto.response;

import com.sahithya.fooddeliverybackend.entity.RestaurantStatus;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public class RegisterRestaurantResponse {

    private final UUID id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final RestaurantStatus status;
    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final AddressResponse address;
    private final Instant createdAt;

    public RegisterRestaurantResponse(
            UUID id,
            String name,
            String email,
            String phoneNumber,
            RestaurantStatus status,
            LocalTime openingTime,
            LocalTime closingTime,
            AddressResponse address,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.address = address;
        this.createdAt = createdAt;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public RestaurantStatus getStatus() {
        return status;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}