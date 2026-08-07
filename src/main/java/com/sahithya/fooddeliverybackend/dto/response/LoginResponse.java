package com.sahithya.fooddeliverybackend.dto.response;

import java.util.UUID;

public class LoginResponse {

    private final UUID id;
    private final String email;
    private final String name;
    private final String accessToken;
    private final String tokenType;

    public LoginResponse(
            UUID id,
            String email,
            String name,
            String accessToken,
            String tokenType
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
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

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}