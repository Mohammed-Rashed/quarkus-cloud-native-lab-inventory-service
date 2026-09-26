package com.mohammed.inventory.messaging;

import com.mohammed.inventory.messaging.event.OrderCreatedEvent;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class OrderEventConsumer {
    @Incoming("order-events-in")
    public void consume(OrderCreatedEvent event) {

        System.out.println(
                "Inventory received order: " + event.orderId()
                        + ", product: " + event.product()
                        + ", quantity: " + event.quantity()
        );
    }

}
