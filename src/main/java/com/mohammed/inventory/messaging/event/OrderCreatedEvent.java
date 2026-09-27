package com.mohammed.inventory.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        String eventType,
        Instant occurredAt,
        int version,
        Long orderId,
        String product,
        int quantity,
        String status
) {
}