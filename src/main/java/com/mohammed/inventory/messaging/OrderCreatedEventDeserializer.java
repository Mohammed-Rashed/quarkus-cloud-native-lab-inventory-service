package com.mohammed.inventory.messaging;

import com.mohammed.inventory.messaging.event.OrderCreatedEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class OrderCreatedEventDeserializer extends ObjectMapperDeserializer<OrderCreatedEvent> {

    public OrderCreatedEventDeserializer() {
        super(OrderCreatedEvent.class);
    }
}