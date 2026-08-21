package com.sahithya.fooddeliverybackend.scheduler;

import com.sahithya.fooddeliverybackend.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InventoryReservationScheduler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InventoryReservationScheduler.class);

    private final InventoryService inventoryService;

    public InventoryReservationScheduler(
            InventoryService inventoryService
    ) {
        this.inventoryService = inventoryService;
    }

    @Scheduled(fixedDelay = 60_000)
    public void releaseExpiredReservationsJob() {
        try {
            inventoryService.releaseExpiredReservations();
        } catch (ObjectOptimisticLockingFailureException exception) {
            LOGGER.warn(
                    "Inventory reservation cleanup encountered a concurrent update; "
                            + "the next run will retry any remaining expired reservations"
            );
        }
    }
}
