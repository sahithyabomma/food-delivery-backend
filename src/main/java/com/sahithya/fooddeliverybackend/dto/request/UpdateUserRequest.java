package com.sahithya.fooddeliverybackend.dto.request;

import jakarta.validation.constraints.Size;

public class UpdateUserRequest {

    @Size(
            min = 2,
            max = 100,
            message = "Name must contain between 2 and 100 characters"
    )
    private String name;

    public UpdateUserRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}