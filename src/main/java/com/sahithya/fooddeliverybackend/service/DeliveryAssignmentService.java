package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.AssignDeliveryPartnerRequest;
import com.sahithya.fooddeliverybackend.dto.request.UpdateDeliveryStatusRequest;
import com.sahithya.fooddeliverybackend.dto.response.DeliveryAssignmentResponse;
import com.sahithya.fooddeliverybackend.entity.*;
import com.sahithya.fooddeliverybackend.exception.*;
import com.sahithya.fooddeliverybackend.mapper.DeliveryAssignmentMapper;
import com.sahithya.fooddeliverybackend.repository.DeliveryAssignmentRepository;
import com.sahithya.fooddeliverybackend.repository.OrderRepository;
import com.sahithya.fooddeliverybackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static com.sahithya.fooddeliverybackend.entity.DeliveryStatus.ASSIGNED;
import static com.sahithya.fooddeliverybackend.entity.DeliveryStatus.PICKED_UP;
import static com.sahithya.fooddeliverybackend.entity.OrderStatus.DELIVERED;

@Service
public class DeliveryAssignmentService {

    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DeliveryAssignmentMapper deliveryAssignmentMapper;
    private final PaymentService paymentService;

    public DeliveryAssignmentService(
            DeliveryAssignmentRepository deliveryAssignmentRepository,
            OrderRepository orderRepository,
            UserRepository userRepository,
            DeliveryAssignmentMapper deliveryAssignmentMapper, PaymentService paymentService
    ) {
        this.deliveryAssignmentRepository = deliveryAssignmentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.deliveryAssignmentMapper = deliveryAssignmentMapper;
        this.paymentService = paymentService;
    }

    @Transactional
    public DeliveryAssignmentResponse assignDeliveryPartner(
            UUID restaurantOwnerUserId,
            UUID orderId,
            AssignDeliveryPartnerRequest request
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        UUID actualOwnerId =
                order.getRestaurant()
                        .getOwner()
                        .getId();

        if (!actualOwnerId.equals(restaurantOwnerUserId)) {
            throw new OrderNotFoundException(orderId);
        }

        if (order.getStatus() != OrderStatus.READY_FOR_PICKUP) {
            throw new OrderNotReadyForDeliveryException(
                    order.getStatus()
            );
        }

        if (deliveryAssignmentRepository.existsByOrderId(orderId)) {
            throw new DeliveryAlreadyAssignedException(orderId);
        }

        UUID deliveryPartnerId =
                request.getDeliveryPartnerId();

        User deliveryPartner = userRepository
                .findById(deliveryPartnerId)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                deliveryPartnerId
                        )
                );

        if (deliveryPartner.getRole()
                != UserRole.DELIVERY_PARTNER) {

            throw new InvalidDeliveryPartnerException(
                    deliveryPartnerId
            );
        }

        Instant now = Instant.now();

        DeliveryAssignment assignment =
                deliveryAssignmentMapper.toEntity(
                        order,
                        deliveryPartner,
                        now
                );

        DeliveryAssignment savedAssignment =
                deliveryAssignmentRepository.save(
                        assignment
                );

        return deliveryAssignmentMapper.toResponse(
                savedAssignment
        );
    }

    @Transactional
    public DeliveryAssignmentResponse updateDeliveryStatus(
            UUID deliveryPartnerUserId,
            UUID orderId,
            UpdateDeliveryStatusRequest request
    ) {

        // 1. Find delivery assignment
        DeliveryAssignment assignment =
                deliveryAssignmentRepository
                        .findByOrderId(orderId)
                        .orElseThrow(
                                () -> new DeliveryAssignmentNotFoundException(
                                        orderId
                                )
                        );

        User deliveryPartner =
                assignment.getDeliveryPartner();

        // 2. Verify the authenticated user is the assigned partner
        if (!deliveryPartner.getId().equals(deliveryPartnerUserId)) {
            throw new DeliveryPartnerNotAuthorizedException();
        }

        // 3. Verify the assigned user actually has DELIVERY_PARTNER role
        if (deliveryPartner.getRole() != UserRole.DELIVERY_PARTNER) {
            throw new DeliveryPartnerNotAuthorizedException();
        }

        // 4. Get associated order
        Order order = assignment.getOrder();

        Instant now = Instant.now();

        DeliveryStatus requestedStatus =
                request.getStatus();

        // 5. DeliveryAssignment validates:
        // ASSIGNED -> PICKED_UP -> DELIVERED
        assignment.changeStatus(
                requestedStatus,
                now
        );

        // 6. Synchronize order status
        switch (requestedStatus) {

            case PICKED_UP ->
                    order.changeStatus(
                            OrderStatus.OUT_FOR_DELIVERY,
                            now
                    );

            case DELIVERED -> {
                order.changeStatus(
                        OrderStatus.DELIVERED,
                        now
                );

                paymentService.completeCashOnDeliveryPayment(
                        order.getId(),
                        now
                );
            }

            case ASSIGNED -> {
                // No transition back to ASSIGNED
            }
        }

        // 7. Hibernate dirty checking persists both entities
        return deliveryAssignmentMapper.toResponse(
                assignment
        );
    }

}