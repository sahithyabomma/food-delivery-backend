package com.sahithya.fooddeliverybackend.repository;

import com.sahithya.fooddeliverybackend.entity.PaymentWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentWebhookEventRepository
        extends JpaRepository<PaymentWebhookEvent, UUID> {

    boolean existsByEventId(String eventId);
}
