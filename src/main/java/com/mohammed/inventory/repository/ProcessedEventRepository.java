package com.mohammed.inventory.repository;

import com.mohammed.inventory.entity.ProcessedEventEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class ProcessedEventRepository implements PanacheRepositoryBase<ProcessedEventEntity, UUID> {

    public boolean isProcessed(UUID eventId) {
        return findByIdOptional(eventId).isPresent();
    }

    @Transactional
    public void markAsProcessed(UUID eventId) {
        ProcessedEventEntity processedEvent = new ProcessedEventEntity();
        processedEvent.eventId = eventId;
        processedEvent.processedAt = LocalDateTime.now();
        persist(processedEvent);
    }
}
