package com.mohammed.inventory.messaging;

import com.mohammed.inventory.messaging.event.StockRejectedEvent;
import com.mohammed.inventory.messaging.event.StockReservedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
public class InventoryEventProducer {
    @Channel("inventory-events-out")
    Emitter<Record<String, Object>> emitter;

    public void sendStockReserved(StockReservedEvent event) {
        emitter.send(
                Record.of(event.orderId().toString(), event)
        );
    }

    public void sendStockRejected(StockRejectedEvent event) {
        emitter.send(
                Record.of(event.orderId().toString(), event)
        );
    }

}
