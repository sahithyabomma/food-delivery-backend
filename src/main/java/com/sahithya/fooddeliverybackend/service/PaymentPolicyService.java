package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.entity.OrderPaymentState;
import com.sahithya.fooddeliverybackend.entity.Payment;
import com.sahithya.fooddeliverybackend.entity.PaymentMethod;
import com.sahithya.fooddeliverybackend.entity.PaymentStatus;
import com.sahithya.fooddeliverybackend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentPolicyService {

    private final PaymentRepository paymentRepository;

    public PaymentPolicyService(
            PaymentRepository paymentRepository
    ) {
        this.paymentRepository = paymentRepository;
    }

    public boolean isPaymentSatisfied(UUID orderId) {

        List<Payment> payments =
                paymentRepository
                        .findByOrderIdOrderByCreatedAtDesc(orderId);

        boolean successfulOnlinePayment =
                payments.stream()
                        .anyMatch(payment ->
                                payment.getStatus()
                                        == PaymentStatus.SUCCESS
                        );

        if (successfulOnlinePayment) {
            return true;
        }

        boolean cashOnDeliverySelected =
                payments.stream()
                        .anyMatch(payment ->
                                payment.getPaymentMethod()
                                        == PaymentMethod.CASH_ON_DELIVERY
                        );

        return cashOnDeliverySelected;
    }

    public OrderPaymentState getPaymentState(UUID orderId) {

        List<Payment> payments =
                paymentRepository
                        .findByOrderIdOrderByCreatedAtDesc(
                                orderId
                        );

        return determinePaymentState(payments);
    }

    public Map<UUID, OrderPaymentState> getPaymentStates(
            List<UUID> orderIds
    ) {
        List<Payment> payments =
                paymentRepository.findByOrderIdIn(
                        orderIds
                );

        Map<UUID, List<Payment>> paymentsByOrder =
                payments.stream()
                        .collect(
                                Collectors.groupingBy(
                                        payment ->
                                                payment.getOrder()
                                                        .getId()
                                )
                        );

        Map<UUID, OrderPaymentState> result =
                new HashMap<>();

        for (UUID orderId : orderIds) {

            List<Payment> orderPayments =
                    paymentsByOrder.getOrDefault(
                            orderId,
                            List.of()
                    );

            result.put(
                    orderId,
                    determinePaymentState(orderPayments)
            );
        }

        return result;
    }

    private OrderPaymentState determinePaymentState(
            List<Payment> payments
    ) {
        boolean paid =
                payments.stream()
                        .anyMatch(payment ->
                                payment.getStatus()
                                        == PaymentStatus.SUCCESS
                        );

        if (paid) {
            return OrderPaymentState.PAID;
        }

        boolean cod =
                payments.stream()
                        .anyMatch(payment ->
                                payment.getPaymentMethod()
                                        == PaymentMethod.CASH_ON_DELIVERY
                        );

        if (cod) {
            return OrderPaymentState.CASH_ON_DELIVERY;
        }

        boolean pending =
                payments.stream()
                        .anyMatch(payment ->
                                payment.getStatus()
                                        == PaymentStatus.PENDING
                        );

        if (pending) {
            return OrderPaymentState.PENDING;
        }

        return OrderPaymentState.UNPAID;
    }
}