package com.sahithya.fooddeliverybackend.service;

import com.sahithya.fooddeliverybackend.dto.request.CreateInventoryRequest;
import com.sahithya.fooddeliverybackend.dto.response.InventoryResponse;
import com.sahithya.fooddeliverybackend.entity.*;
import com.sahithya.fooddeliverybackend.exception.InventoryAlreadyExistsException;
import com.sahithya.fooddeliverybackend.exception.InventoryNotFoundException;
import com.sahithya.fooddeliverybackend.exception.MenuItemNotFoundException;
import com.sahithya.fooddeliverybackend.exception.UnauthorizedRestaurantOperationException;
import com.sahithya.fooddeliverybackend.repository.InventoryRepository;
import com.sahithya.fooddeliverybackend.repository.InventoryReservationRepository;
import com.sahithya.fooddeliverybackend.repository.MenuItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final long RESERVATION_TTL_SECONDS = 10 * 60;
    private static final int EXPIRATION_BATCH_SIZE = 100;

    private final InventoryRepository inventoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final InventoryReservationRepository
            inventoryReservationRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            MenuItemRepository menuItemRepository,
            InventoryReservationRepository inventoryReservationRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.menuItemRepository = menuItemRepository;
        this.inventoryReservationRepository = inventoryReservationRepository;
    }

    @Transactional
    public InventoryResponse createInventory(
            UUID ownerUserId,
            UUID menuItemId,
            CreateInventoryRequest request
    ) {

        MenuItem menuItem =
                menuItemRepository.findById(menuItemId)
                        .orElseThrow(
                                () -> new MenuItemNotFoundException(
                                        menuItemId
                                )
                        );

        UUID actualOwnerId =
                menuItem.getCategory()
                        .getRestaurant()
                        .getOwner()
                        .getId();

        if (!actualOwnerId.equals(ownerUserId)) {
            throw new UnauthorizedRestaurantOperationException();
        }

        if (inventoryRepository.existsByMenuItemId(menuItemId)) {
            throw new InventoryAlreadyExistsException(menuItemId);
        }

        Instant now = Instant.now();

        Inventory inventory =
                new Inventory(
                        UUID.randomUUID(),
                        menuItem,
                        request.getAvailableQuantity(),
                        0,
                        now,
                        now
                );

        Inventory saved =
                inventoryRepository.save(inventory);

        return toResponse(saved);
    }

    @Transactional
    public InventoryResponse restock(
            UUID ownerUserId,
            UUID menuItemId,
            int quantity
    ) {
        Inventory inventory =
                inventoryRepository
                        .findByMenuItemId(menuItemId)
                        .orElseThrow(
                                () -> new InventoryNotFoundException(
                                        menuItemId
                                )
                        );

        UUID actualOwnerId =
                inventory.getMenuItem()
                        .getCategory()
                        .getRestaurant()
                        .getOwner()
                        .getId();

        if (!actualOwnerId.equals(ownerUserId)) {
            throw new UnauthorizedRestaurantOperationException();
        }

        inventory.restock(
                quantity,
                Instant.now()
        );

        return toResponse(inventory);
    }

    private InventoryResponse toResponse(
            Inventory inventory
    ) {
        return new InventoryResponse(
                inventory.getMenuItem().getId(),
                inventory.getAvailableQuantity(),
                inventory.getReservedQuantity()
        );
    }

    @Transactional
    public void reserveForOrder(Order order) {

        List<OrderItem> orderItems =
                order.getOrderItems();

        List<UUID> menuItemIds =
                orderItems.stream()
                        .map(OrderItem::getMenuItemId)
                        .toList();

        List<Inventory> inventories =
                inventoryRepository.findByMenuItemIdIn(
                        menuItemIds
                );

        Map<UUID, Inventory> inventoryByMenuItem =
                inventories.stream()
                        .collect(
                                Collectors.toMap(
                                        inventory ->
                                                inventory.getMenuItem().getId(),
                                        inventory -> inventory
                                )
                        );

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(
                RESERVATION_TTL_SECONDS
        );

        List<InventoryReservation> reservations =
                new ArrayList<>();

        for (OrderItem orderItem : orderItems) {

            UUID menuItemId =
                    orderItem.getMenuItemId();

            Inventory inventory =
                    inventoryByMenuItem.get(menuItemId);

            if (inventory == null) {
                throw new InventoryNotFoundException(
                        menuItemId
                );
            }

            inventory.reserve(
                    orderItem.getQuantity(),
                    now
            );

            InventoryReservation reservation =
                    new InventoryReservation(
                            UUID.randomUUID(),
                            order,
                            inventory.getMenuItem(),
                            orderItem.getQuantity(),
                            InventoryReservationStatus.RESERVED,
                            now,
                            now,
                            expiresAt
                    );

            reservations.add(reservation);
        }

        inventoryReservationRepository.saveAll(
                reservations
        );
    }

    @Transactional
    public void confirmReservationsForOrder(
            UUID orderId,
            Instant now
    ) {
        List<InventoryReservation> reservations =
                inventoryReservationRepository
                        .findByOrderIdAndStatus(
                                orderId,
                                InventoryReservationStatus.RESERVED
                        );

        for (InventoryReservation reservation : reservations) {

            UUID menuItemId =
                    reservation.getMenuItem().getId();

            Inventory inventory =
                    inventoryRepository
                            .findByMenuItemId(menuItemId)
                            .orElseThrow(
                                    () -> new InventoryNotFoundException(
                                            menuItemId
                                    )
                            );

            inventory.confirm(
                    reservation.getQuantity(),
                    now
            );

            reservation.confirm(now);
        }
    }

    @Transactional
    public void releaseReservationsForOrder(
            UUID orderId,
            Instant now
    ) {
        List<InventoryReservation> reservations =
                inventoryReservationRepository
                        .findByOrderIdAndStatus(
                                orderId,
                                InventoryReservationStatus.RESERVED
                        );

        for (InventoryReservation reservation : reservations) {

            UUID menuItemId =
                    reservation.getMenuItem().getId();

            Inventory inventory =
                    inventoryRepository
                            .findByMenuItemId(menuItemId)
                            .orElseThrow(
                                    () -> new InventoryNotFoundException(
                                            menuItemId
                                    )
                            );

            inventory.release(
                    reservation.getQuantity(),
                    now
            );

            reservation.release(now);
        }
    }

    @Transactional
    public void releaseExpiredReservations() {

        Instant now = Instant.now();

        Pageable firstBatch = PageRequest.of(
                0,
                EXPIRATION_BATCH_SIZE,
                Sort.by(Sort.Direction.ASC, "expiresAt")
        );

        Page<InventoryReservation> page =
                inventoryReservationRepository
                        .findByStatusAndExpiresAtBefore(
                                InventoryReservationStatus.RESERVED,
                                now,
                                firstBatch
                        );

        for (InventoryReservation reservation : page.getContent()) {

            UUID menuItemId =
                    reservation.getMenuItem().getId();

            Inventory inventory =
                    inventoryRepository
                            .findByMenuItemId(menuItemId)
                            .orElseThrow(
                                    () -> new InventoryNotFoundException(
                                            menuItemId
                                    )
                            );

            inventory.release(
                    reservation.getQuantity(),
                    now
            );

            reservation.release(now);
        }
    }
}
