package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.RegisterUserRequest;
import com.sahithya.fooddeliverybackend.dto.request.UpdateUserRequest;
import com.sahithya.fooddeliverybackend.dto.response.RegisterUserResponse;
import com.sahithya.fooddeliverybackend.dto.response.UserResponse;
import com.sahithya.fooddeliverybackend.entity.User;
import com.sahithya.fooddeliverybackend.exception.EmailAlreadyExistsException;
import com.sahithya.fooddeliverybackend.exception.InvalidUserUpdateException;
import com.sahithya.fooddeliverybackend.exception.UserNotFoundException;
import com.sahithya.fooddeliverybackend.mapper.UserMapper;
import com.sahithya.fooddeliverybackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public RegisterUserResponse register(final RegisterUserRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException(normalizedEmail);
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());
        Instant now = Instant.now();
        User user = userMapper.toEntity(request, normalizedEmail, passwordHash, now);
        User saveUser = userRepository.save(user);
        return userMapper.toRegisterUserResponse(saveUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(
            UUID id,
            UpdateUserRequest request
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (request.getName() != null) {
            String normalizedName = request.getName().trim();


                if (normalizedName.isEmpty()) {
                    throw new InvalidUserUpdateException("Name cannot be blank");
                }


            user.updateName(normalizedName, Instant.now());
        }

        return userMapper.toUserResponse(user);
    }

}
