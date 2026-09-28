package com.mohammed.inventory.messaging;

import com.mohammed.exception.InsufficientStockException;
import com.mohammed.inventory.messaging.event.OrderCreatedEvent;
import com.mohammed.inventory.messaging.event.StockRejectedEvent;
import com.mohammed.inventory.messaging.event.StockReservedEvent;
import com.mohammed.inventory.repository.InventoryRepository;
import com.mohammed.inventory.repository.ProcessedEventRepository;
import com.mohammed.inventory.service.InventoryService;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.time.Instant;
import java.util.UUID;

public class OrderEventConsumer {
    private final ProcessedEventRepository processedEventRepository;
    private final InventoryService inventoryService;
    private final InventoryEventProducer inventoryEventProducer;
    public OrderEventConsumer(
            ProcessedEventRepository processedEventRepository,
            InventoryService inventoryService,
            InventoryEventProducer inventoryEventProducer
    ) {
        this.processedEventRepository = processedEventRepository;
        this.inventoryService = inventoryService;
        this.inventoryEventProducer = inventoryEventProducer;
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



        try {

            inventoryService.reserveStock(
                    event.productId(),
                    event.quantity()
            );
            StockReservedEvent stockReservedEvent = new StockReservedEvent(
                    UUID.randomUUID(),
                    "STOCK_RESERVED",
                    Instant.now(),
                    1,
                    event.orderId(),
                    event.productId(),
                    event.quantity()
            );

            inventoryEventProducer.sendStockReserved(stockReservedEvent);

        } catch (InsufficientStockException e) {
            String stockType="STOCK_REJECTED";

            StockRejectedEvent stockRejectedEvent = new StockRejectedEvent(
                    UUID.randomUUID(),
                    "STOCK_REJECTED",
                    Instant.now(),
                    1,
                    event.orderId(),
                    event.productId(),
                    event.quantity(),
                    e.getMessage()
            );
            inventoryEventProducer.sendStockRejected(stockRejectedEvent);

        }
        processedEventRepository.markAsProcessed(event.eventId());


        System.out.println(
                "Event marked as processed: " + event.eventId()
        );



    }

}
