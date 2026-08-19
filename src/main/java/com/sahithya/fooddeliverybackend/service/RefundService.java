package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.RefundResultRequest;
import com.sahithya.fooddeliverybackend.dto.response.RefundResponse;
import com.sahithya.fooddeliverybackend.entity.Order;
import com.sahithya.fooddeliverybackend.entity.Refund;
import com.sahithya.fooddeliverybackend.entity.RefundStatus;
import com.sahithya.fooddeliverybackend.exception.*;
import com.sahithya.fooddeliverybackend.mapper.RefundMapper;
import com.sahithya.fooddeliverybackend.repository.OrderRepository;
import com.sahithya.fooddeliverybackend.repository.RefundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final RefundMapper refundMapper;
    private final OrderRepository orderRepository;

    public RefundService(
            RefundRepository refundRepository,
            RefundMapper refundMapper, OrderRepository orderRepository
    ) {
        this.refundRepository = refundRepository;
        this.refundMapper = refundMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public RefundResponse updateRefundResult(
            UUID refundId,
            RefundResultRequest request
    ) {
        Refund refund = refundRepository
                .findById(refundId)
                .orElseThrow(
                        () -> new RefundNotFoundException(refundId)
                );

        Instant now = Instant.now();

        switch (request.getStatus()) {

            case SUCCESS -> {

                if (request.getGatewayRefundReference() == null
                        || request.getGatewayRefundReference().isBlank()) {

                    throw new RefundReferenceRequiredException();
                }

                refund.markSuccess(
                        request.getGatewayRefundReference(),
                        now
                );
            }

            case FAILED ->
                    refund.markFailed(now);

            case PENDING ->
                    throw new InvalidRefundStatusTransitionException(
                            refund.getStatus(),
                            RefundStatus.PENDING
                    );
        }

        return refundMapper.toResponse(refund);
    }

    @Transactional(readOnly = true)
    public RefundResponse getRefundForOrder(
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

        Refund refund = refundRepository
                .findByOrderId(order.getId())
                .orElseThrow(
                        () -> new RefundNotFoundForOrderException(orderId)
                );

        return refundMapper.toResponse(refund);
    }

    public Map<UUID, RefundStatus> getRefundStatuses(
            List<UUID> orderIds
    ) {
        List<Refund> refunds =
                refundRepository.findByOrderIdIn(orderIds);

        return refunds.stream()
                .collect(
                        Collectors.toMap(
                                refund -> refund.getOrder().getId(),
                                Refund::getStatus
                        )
                );
    }
}