package com.mohammed.inventory.messaging;

import com.mohammed.inventory.messaging.event.OrderCreatedEvent;
import com.mohammed.inventory.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class OrderEventConsumer {
    private final ProcessedEventRepository processedEventRepository;
    public OrderEventConsumer(ProcessedEventRepository processedEventRepository) {
        this.processedEventRepository = processedEventRepository;
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
        processedEventRepository.markAsProcessed(event.eventId());

        System.out.println(
                "Event marked as processed: " + event.eventId()
        );
    }

}
