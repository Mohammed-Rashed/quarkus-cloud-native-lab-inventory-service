package com.mohammed.inventory.messaging;

import com.mohammed.inventory.messaging.event.OrderCreatedEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

public class OrderCreatedEventSerializer  extends ObjectMapperSerializer<OrderCreatedEvent> {
}
