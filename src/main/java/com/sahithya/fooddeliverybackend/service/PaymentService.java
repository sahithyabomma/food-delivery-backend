package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.CreatePaymentRequest;
import com.sahithya.fooddeliverybackend.dto.request.PaymentResultRequest;
import com.sahithya.fooddeliverybackend.dto.response.PaymentResponse;
import com.sahithya.fooddeliverybackend.entity.*;
import com.sahithya.fooddeliverybackend.exception.*;
import com.sahithya.fooddeliverybackend.mapper.PaymentMapper;
import com.sahithya.fooddeliverybackend.repository.InventoryRepository;
import com.sahithya.fooddeliverybackend.repository.OrderRepository;
import com.sahithya.fooddeliverybackend.repository.PaymentRepository;
import com.sahithya.fooddeliverybackend.repository.PaymentWebhookEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sahithya.fooddeliverybackend.entity.PaymentStatus.*;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentSignatureService paymentSignatureService;
    private final PaymentWebhookEventRepository paymentWebhookEventRepository;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            PaymentMapper paymentMapper, PaymentSignatureService paymentSignatureService,
            PaymentWebhookEventRepository paymentWebhookEventRepository,
            InventoryService inventoryService,
            OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
        this.paymentSignatureService = paymentSignatureService;
        this.paymentWebhookEventRepository = paymentWebhookEventRepository;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    @Transactional
    public PaymentResponse createPayment(
            UUID userId,
            UUID orderId,
            String idempotencyKey,
            CreatePaymentRequest request
    ) {
        Optional<Payment> existingPayment =
                paymentRepository.findByIdempotencyKey(
                        idempotencyKey
                );

        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();

            boolean sameOrder =
                    payment.getOrder()
                            .getId()
                            .equals(orderId);

            boolean sameMethod =
                    payment.getPaymentMethod()
                            == request.getPaymentMethod();

            if (!sameOrder || !sameMethod) {
                throw new IdempotencyKeyConflictException(
                        idempotencyKey
                );
            }

            return paymentMapper.toResponse(payment);
        }

        Order order = orderRepository
                .findByIdAndUserId(
                        orderId,
                        userId
                )
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new PaymentNotAllowedException(
                    "Cannot create payment for a cancelled order"
            );
        }

        boolean alreadyPaid =
                paymentRepository.existsByOrderIdAndStatus(
                        orderId,
                        PaymentStatus.SUCCESS
                );

        if (alreadyPaid) {
            throw new OrderAlreadyPaidException(orderId);
        }

        Instant now = Instant.now();

        Payment payment =
                paymentMapper.toEntity(
                        order,
                        request,
                        idempotencyKey,
                        now
                );

        Payment savedPayment =
                paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse updatePaymentResult(
            UUID paymentId,
            String timestamp,
            String signature,
            PaymentResultRequest request
    ) {

        String transactionReference =
                request.getTransactionReference() == null
                        ? ""
                        : request.getTransactionReference();

        String payload =
                request.getEventId()
                        + ":"
                        + paymentId
                        + ":"
                        + request.getStatus()
                        + ":"
                        + transactionReference;

        paymentSignatureService.verifySignature(
                payload,
                timestamp,
                signature
        );

        /*
         * Same gateway event already processed.
         */
        if (paymentWebhookEventRepository.existsByEventId(
                request.getEventId()
        )) {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(
                            () -> new PaymentNotFoundException(paymentId)
                    );

            return paymentMapper.toResponse(payment);
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(
                        () -> new PaymentNotFoundException(paymentId)
                );

        Instant now = Instant.now();

        switch (request.getStatus()) {

            case SUCCESS -> {

                if (transactionReference.isBlank()) {
                    throw new PaymentTransactionReferenceRequiredException();
                }

                Optional<Payment> existing =
                        paymentRepository.findByTransactionReference(
                                transactionReference
                        );

                if (existing.isPresent()
                        && !existing.get().getId().equals(paymentId)) {

                    throw new DuplicateTransactionReferenceException(
                            transactionReference
                    );
                }

                payment.markSuccess(
                        transactionReference,
                        now
                );
                inventoryService.confirmReservationsForOrder(
                        payment.getOrder().getId(),
                        now
                );
            }

            case FAILED -> {
                payment.markFailed(now);
                inventoryService.releaseReservationsForOrder(
                        payment.getOrder().getId(),
                        now
                );
            }

            case PENDING ->
                    throw new InvalidPaymentStatusTransitionException(
                            payment.getStatus(),
                            PaymentStatus.PENDING
                    );
        }

        PaymentWebhookEvent event =
                new PaymentWebhookEvent(
                        UUID.randomUUID(),
                        request.getEventId(),
                        payment,
                        now
                );

        paymentWebhookEventRepository.save(event);

        return paymentMapper.toResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentHistory(
            UUID userId,
            UUID orderId
    ) {
        Order order = orderRepository
                .findByIdAndUserId(
                        orderId,
                        userId
                )
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        List<Payment> payments =
                paymentRepository
                        .findByOrderIdOrderByCreatedAtDesc(
                                order.getId()
                        );

        return payments.stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Transactional
    public void completeCashOnDeliveryPayment(
            UUID orderId,
            Instant now
    ) {

        Optional<Payment> codPayment =
                paymentRepository
                        .findFirstByOrderIdAndPaymentMethodOrderByCreatedAtDesc(
                                orderId,
                                PaymentMethod.CASH_ON_DELIVERY
                        );

        if (codPayment.isEmpty()) {
            return;
        }

        Payment payment = codPayment.get();

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new InvalidPaymentStatusTransitionException(
                    payment.getStatus(),
                    PaymentStatus.SUCCESS
            );
        }

        payment.markSuccess(
                "COD-" + orderId,
                now
        );
    }
}