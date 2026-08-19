package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.Payment;
import com.sahithya.fooddeliverybackend.entity.PaymentMethod;
import com.sahithya.fooddeliverybackend.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository
        extends JpaRepository<Payment, UUID> {

    boolean existsByOrderIdAndStatus(
            UUID orderId,
            PaymentStatus status
    );

    List<Payment> findByOrderIdOrderByCreatedAtDesc(
            UUID orderId
    );

    Optional<Payment> findByIdempotencyKey(
            String idempotencyKey
    );

    boolean existsByTransactionReference(
            String transactionReference
    );

    Optional<Payment> findByTransactionReference(
            String transactionReference
    );

    Optional<Payment> findFirstByOrderIdAndPaymentMethodOrderByCreatedAtDesc(
            UUID orderId,
            PaymentMethod paymentMethod
    );

    List<Payment> findByOrderIdIn(
            List<UUID> orderIds
    );

    Optional<Payment> findFirstByOrderIdAndStatusOrderByCreatedAtDesc(
            UUID orderId,
            PaymentStatus status
    );
}