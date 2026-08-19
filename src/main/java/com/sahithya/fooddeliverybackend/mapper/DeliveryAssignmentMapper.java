package com.sahithya.fooddeliverybackend.mapper;

import com.sahithya.fooddeliverybackend.dto.response.DeliveryAssignmentResponse;
import com.sahithya.fooddeliverybackend.entity.DeliveryAssignment;
import com.sahithya.fooddeliverybackend.entity.DeliveryStatus;
import com.sahithya.fooddeliverybackend.entity.Order;
import com.sahithya.fooddeliverybackend.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class DeliveryAssignmentMapper {

    public DeliveryAssignment toEntity(
            Order order,
            User deliveryPartner,
            Instant now
    ) {
        return new DeliveryAssignment(
                UUID.randomUUID(),
                order,
                deliveryPartner,
                DeliveryStatus.ASSIGNED,
                now
        );
    }


    public DeliveryAssignmentResponse toResponse(
            DeliveryAssignment assignment
    ) {
        return new DeliveryAssignmentResponse(
                assignment.getId(),
                assignment.getOrder().getId(),
                assignment.getDeliveryPartner().getId(),
                assignment.getStatus(),
                assignment.getAssignedAt(),
                assignment.getPickedUpAt(),
                assignment.getDeliveredAt()
        );
    }
}