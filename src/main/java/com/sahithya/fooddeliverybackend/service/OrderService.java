package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.UpdateOrderStatusRequest;
import com.sahithya.fooddeliverybackend.dto.response.*;
import com.sahithya.fooddeliverybackend.entity.*;
import com.sahithya.fooddeliverybackend.exception.*;
import com.sahithya.fooddeliverybackend.mapper.OrderMapper;
import com.sahithya.fooddeliverybackend.mapper.RefundMapper;
import com.sahithya.fooddeliverybackend.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentPolicyService paymentPolicyService;
    private final RefundRepository refundRepository;
    private final RefundMapper refundMapper;
    private final RefundService refundService;
    private final InventoryService  inventoryService;
    private final InventoryReservationRepository inventoryReservationRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            OrderItemRepository orderItemRepository,
            RestaurantRepository restaurantRepository,
            PaymentRepository paymentRepository,
            PaymentPolicyService paymentPolicyService,
            RefundRepository refundRepository,
            RefundMapper refundMapper,
            RefundService refundService,
            InventoryService inventoryService,
            InventoryReservationRepository inventoryReservationRepository,
             InventoryRepository inventoryRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemRepository = orderItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.paymentRepository = paymentRepository;
        this.paymentPolicyService = paymentPolicyService;
        this.refundRepository = refundRepository;
        this.refundMapper = refundMapper;
        this.refundService = refundService;
        this.inventoryService = inventoryService;
        this.inventoryReservationRepository = inventoryReservationRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public CheckoutResponse checkout(UUID userId) {

        Instant now = Instant.now();

        Cart cart = cartRepository
                .findByUserIdAndCartStatus(
                        userId,
                        CartStatus.ACTIVE
                )
                .orElseThrow(ActiveCartNotFoundException::new);

        List<CartItem> cartItems =
                cartItemRepository.findAllByCartIdWithMenuItem(
                        cart.getId()
                );

        if (cartItems.isEmpty()) {
            throw new EmptyCartException();
        }

        for (CartItem cartItem : cartItems) {
            MenuItem menuItem = cartItem.getMenuItem();

            if (menuItem.isDeleted() || !menuItem.isAvailable()) {
                throw new MenuItemNotAvailableException(
                        menuItem.getId()
                );
            }
        }

        BigDecimal totalAmount = cartItems.stream()
                .map(cartItem -> {
                    MenuItem menuItem = cartItem.getMenuItem();

                    return menuItem.getPrice().multiply(
                            BigDecimal.valueOf(
                                    cartItem.getQuantity()
                            )
                    );
                })
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        Order order = orderMapper.toOrderEntity(
                cart.getUser(),
                cart.getRestaurant(),
                totalAmount,
                now
        );

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem =
                    orderMapper.toOrderItemEntity(
                            cartItem,
                            now
                    );

            order.addOrderItem(orderItem);
        }



        Order savedOrder =
                orderRepository.save(order);
        inventoryService.reserveForOrder(
                order
        );

        cartItemRepository.deleteAll(cartItems);

        cart.clearRestaurant(now);

        return orderMapper.toCheckoutResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderSummaryResponse> getOrderHistory(
            UUID userId,
            int page,
            int size
    ) {
        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.DESC,
                                "createdAt"
                        )
                );

        Page<Order> orderPage =
                orderRepository.findUserOrdersWithRestaurant(
                        userId,
                        pageable
                );

        List<Order> orders =
                orderPage.getContent();

        if (orders.isEmpty()) {
            return new PageResponse<>(
                    List.of(),
                    page,
                    size,
                    orderPage.getTotalElements(),
                    orderPage.getTotalPages(),
                    orderPage.isLast()
            );
        }

        List<UUID> orderIds =
                orders.stream()
                        .map(Order::getId)
                        .toList();
        Map<UUID, OrderPaymentState> paymentStates =
                paymentPolicyService.getPaymentStates(orderIds);

        Map<UUID, RefundStatus> refundStatuses =
                refundService.getRefundStatuses(orderIds);


        List<OrderSummaryResponse> content =
                orders.stream()
                        .map(order ->
                                orderMapper.toOrderSummaryResponse(
                                        order,
                                        paymentStates.get(order.getId()),
                                        refundStatuses.get(order.getId())
                                )
                        )
                        .toList();

        return new PageResponse<>(
                content,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast()
        );
    }

    @Transactional(readOnly = true)
    public OrderDetailsResponse getOrderDetails(
            UUID userId,
            UUID orderId
    ) {
        Order order = orderRepository
                .findByIdAndUserId(orderId, userId)
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(orderId);

        OrderPaymentState paymentState =
                paymentPolicyService.getPaymentState(orderId);

        RefundSummaryResponse refundResponse =
                refundRepository
                        .findByOrderId(orderId)
                        .map(refundMapper::toSummaryResponse)
                        .orElse(null);

        return orderMapper.toOrderDetailsResponse(
                order,
                orderItems,
                paymentState,
                refundResponse
        );
    }

    @Transactional
    public UpdateOrderStatusResponse updateOrderStatus(
            UUID userId,
            UserRole role,
            UUID orderId,
            UpdateOrderStatusRequest request
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new OrderNotFoundException(orderId)
                );

        validateRolePermission(
                userId,
                role,
                order,
                request.getStatus()
        );

        if (request.getStatus() == OrderStatus.CONFIRMED) {

            boolean paymentSatisfied =
                    paymentPolicyService.isPaymentSatisfied(orderId);
            if (!paymentSatisfied) {
                throw new OrderPaymentRequiredException(orderId);
            }
        }

        Instant now = Instant.now();

        order.changeStatus(
                request.getStatus(),
                now
        );

        if (request.getStatus() == OrderStatus.CONFIRMED) {
            inventoryService.confirmReservationsForOrder(
                    orderId,
                    now
            );
        }

        return orderMapper.toUpdateStatusResponse(order);
    }

    private void validateRolePermission(
            UUID userId,
            UserRole role,
            Order order,
            OrderStatus requestedStatus
    ) {
        switch (role) {

            case CUSTOMER -> {

                if (!order.getUser().getId().equals(userId)) {
                    throw new OrderNotFoundException(order.getId());
                }

                if (requestedStatus != OrderStatus.CANCELLED) {
                    throw new OrderStatusPermissionException();
                }
            }

            case RESTAURANT_OWNER -> {

                UUID restaurantOwnerId =
                        order.getRestaurant()
                                .getOwner()
                                .getId();

                if (!restaurantOwnerId.equals(userId)) {
                    throw new OrderNotFoundException(order.getId());
                }

                boolean allowed =
                        requestedStatus == OrderStatus.CONFIRMED
                                || requestedStatus == OrderStatus.PREPARING
                                || requestedStatus ==
                                OrderStatus.READY_FOR_PICKUP;

                if (!allowed) {
                    throw new OrderStatusPermissionException();
                }
            }

            case DELIVERY_PARTNER -> {

                boolean allowed =
                        requestedStatus ==
                                OrderStatus.OUT_FOR_DELIVERY
                                || requestedStatus ==
                                OrderStatus.DELIVERED;

                if (!allowed) {
                    throw new OrderStatusPermissionException();
                }
            }

            case ADMIN -> {
                // domain transition rules still apply
            }
        }
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderSummaryResponse> getRestaurantOrders(
            UUID ownerUserId,
            UUID restaurantId,
            int page,
            int size
    ) {
        Restaurant restaurant =
                restaurantRepository
                        .findByIdAndOwnerId(
                                restaurantId,
                                ownerUserId
                        )
                        .orElseThrow(
                                () -> new RestaurantNotFoundException(
                                        restaurantId
                                )
                        );

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.DESC,
                                "createdAt"
                        )
                );

        Page<Order> orderPage =
                orderRepository
                        .findRestaurantOrdersWithRestaurant(
                                restaurant.getId(),
                                pageable
                        );

        List<Order> orders =
                orderPage.getContent();

        if (orders.isEmpty()) {
            return new PageResponse<>(
                    List.of(),
                    orderPage.getNumber(),
                    orderPage.getSize(),
                    orderPage.getTotalElements(),
                    orderPage.getTotalPages(),
                    orderPage.isLast()
            );
        }

        List<UUID> orderIds =
                orders.stream()
                        .map(Order::getId)
                        .toList();
        Map<UUID, OrderPaymentState> paymentStates =
                paymentPolicyService.getPaymentStates(orderIds);

        Map<UUID, RefundStatus> refundStatuses =
                refundService.getRefundStatuses(orderIds);


        List<OrderSummaryResponse> content =
                orders.stream()
                        .map(order ->


                                orderMapper.toOrderSummaryResponse(
                                        order,
                                        paymentStates.get(order.getId()),
                                        refundStatuses.get(order.getId())
                                )
                        )
                        .toList();

        return new PageResponse<>(
                content,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast()
        );
    }

    @Transactional
    public void cancelOrder(
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

        Instant now = Instant.now();

        // 1. Validate + cancel order
        order.cancel(now);

        inventoryService.releaseReservationsForOrder(
                orderId,
                now
        );

        // 2. Look for a successful payment
        Optional<Payment> successfulPayment =
                paymentRepository
                        .findFirstByOrderIdAndStatusOrderByCreatedAtDesc(
                                orderId,
                                PaymentStatus.SUCCESS
                        );

        // 3. No successful payment -> nothing to refund
        if (successfulPayment.isEmpty()) {
            return;
        }

        Payment payment = successfulPayment.get();

        // 4. Prevent duplicate refund creation
        if (refundRepository.existsByPaymentId(payment.getId())) {
            return;
        }

        // 5. Create refund
        Refund refund =
                refundMapper.toEntity(
                        payment,
                        order,
                        "ORDER_CANCELLED",
                        now
                );

        refundRepository.save(refund);
    }


}