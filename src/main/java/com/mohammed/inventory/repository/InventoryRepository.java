package com.mohammed.inventory.repository;

import com.mohammed.inventory.entity.InventoryEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class InventoryRepository implements PanacheRepository {
    public Optional<InventoryEntity> findByProductId(Long productId) {
        return find("productId", productId).firstResultOptional();
    }
}
