package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.request.RegisterUserRequest;
import com.sahithya.fooddeliverybackend.dto.response.RegisterUserResponse;
import com.sahithya.fooddeliverybackend.dto.response.UserResponse;
import com.sahithya.fooddeliverybackend.entity.User;
import com.sahithya.fooddeliverybackend.entity.UserRole;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class UserMapper {
    public User toEntity(RegisterUserRequest request,
                         String normalizedEmail,
                         String passwordHash,
                         Instant now) {
        return new User(
                UUID.randomUUID(),
                request.getName().trim(),
                normalizedEmail,
                passwordHash,
                UserRole.CUSTOMER,
                now,
                now
        );

    }

    public RegisterUserResponse toRegisterUserResponse(
            User user
    ) {
        return new RegisterUserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt(),
                user.getRole()
        );
    }

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt()
        );
    }
}
