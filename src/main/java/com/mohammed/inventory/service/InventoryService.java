package com.mohammed.inventory.service;

import com.mohammed.inventory.entity.InventoryEntity;
import com.mohammed.inventory.repository.InventoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    @Transactional
    public void reserveStock(Long productId, int quantity) {
        InventoryEntity inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found in inventory")
                );

        if (inventory.availableQuantity < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.availableQuantity -= quantity;
        inventory.reservedQuantity += quantity;
    }
}
