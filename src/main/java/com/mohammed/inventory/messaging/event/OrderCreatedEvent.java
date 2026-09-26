package com.mohammed.inventory.messaging.event;

public record OrderCreatedEvent(
        Long orderId,
        String product,
        int quantity,
        String status
) {
}