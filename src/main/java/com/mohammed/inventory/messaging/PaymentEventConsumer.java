package com.mohammed.inventory.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohammed.inventory.messaging.event.PaymentFailedEvent;
import com.mohammed.inventory.repository.ProcessedEventRepository;
import com.mohammed.inventory.service.InventoryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PaymentEventConsumer {
    private final ObjectMapper objectMapper;
    private final InventoryService inventoryService;
    private final ProcessedEventRepository processedEventRepository;

    public PaymentEventConsumer(
            ObjectMapper objectMapper,
            InventoryService inventoryService,
            ProcessedEventRepository processedEventRepository
    ) {
        this.objectMapper = objectMapper;
        this.inventoryService = inventoryService;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    @Incoming("payment-events-in")
    public void consume(String message) throws Exception {

        JsonNode json = objectMapper.readTree(message);

        String eventType = json.get("eventType").asText();

        if (!"PAYMENT_FAILED".equals(eventType)) {
            return;
        }

        PaymentFailedEvent event = objectMapper.readValue(
                message,
                PaymentFailedEvent.class
        );
        if (processedEventRepository.isProcessed(event.eventId())) {
            System.out.println(
                    "Duplicate PAYMENT_FAILED, skipping: "
                            + event.eventId()
            );
            return;
        }
        inventoryService.releaseStock(
                event.productId(),
                event.quantity()
        );

        System.out.println(
                "Stock released for order: " + event.orderId()
        );
        System.out.println(
                "Inventory received PAYMENT_FAILED for order: "
                        + event.orderId()
        );

    }
}
