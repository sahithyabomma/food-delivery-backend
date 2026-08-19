package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefundRepository
        extends JpaRepository<Refund, UUID> {

    boolean existsByPaymentId(UUID paymentId);

    Optional<Refund> findByOrderId(UUID orderId);

    List<Refund> findByOrderIdIn(
            List<UUID> orderIds
    );

}