package com.sahithya.fooddeliverybackend.dto.request;

import com.sahithya.fooddeliverybackend.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

public class RegisterUserRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 64,
            message = "Password must be between 8 and 64 characters")
    private String password;

    @NonNull
    private UserRole userRole;

    public RegisterUserRequest() {
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public UserRole getUserRole() { return this.userRole; }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
}
}
