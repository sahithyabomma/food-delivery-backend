package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository
        extends JpaRepository<Restaurant, UUID> {

    boolean existsByEmail(String email);

    Optional<Restaurant> findByEmail(String email);
}