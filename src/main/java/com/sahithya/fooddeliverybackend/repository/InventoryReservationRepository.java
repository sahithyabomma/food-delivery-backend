package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.Inventory;
import com.sahithya.fooddeliverybackend.entity.InventoryReservation;
import com.sahithya.fooddeliverybackend.entity.InventoryReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InventoryReservationRepository
        extends JpaRepository<InventoryReservation, UUID> {

    List<InventoryReservation>
    findByOrderIdAndStatus(
            UUID orderId,
            InventoryReservationStatus status
    );

    boolean existsByOrderIdAndMenuItemId(
            UUID orderId,
            UUID menuItemId
    );

    Page<InventoryReservation> findByStatusAndExpiresAtBefore(
            InventoryReservationStatus status,
            Instant time,
            Pageable pageable
    );
}
