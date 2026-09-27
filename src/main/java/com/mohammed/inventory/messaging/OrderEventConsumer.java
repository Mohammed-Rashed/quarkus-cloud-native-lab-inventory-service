package com.mohammed.inventory.messaging;

import com.mohammed.inventory.messaging.event.OrderCreatedEvent;
import com.mohammed.inventory.repository.InventoryRepository;
import com.mohammed.inventory.repository.ProcessedEventRepository;
import com.mohammed.inventory.service.InventoryService;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class OrderEventConsumer {
    private final ProcessedEventRepository processedEventRepository;
    private final InventoryService inventoryService;
    public OrderEventConsumer(ProcessedEventRepository  processedEventRepository,
                              InventoryService inventoryService
            ) {
        this.processedEventRepository = processedEventRepository;
        this.inventoryService = inventoryService;
    }
    @Incoming("order-events-in")
    @Transactional
    public void consume(OrderCreatedEvent event) {

        if (processedEventRepository.isProcessed(event.eventId())) {
            System.out.println(
                    "Duplicate event detected, skipping: " + event.eventId()
            );
            return;
        }

        System.out.println(
                "Processing new event: " + event.eventId()
                        + " for order: " + event.orderId()
        );

        inventoryService.reserveStock(event.productId(), event.quantity());

        processedEventRepository.markAsProcessed(event.eventId());

        System.out.println(
                "Event marked as processed: " + event.eventId()
        );
    }

}
