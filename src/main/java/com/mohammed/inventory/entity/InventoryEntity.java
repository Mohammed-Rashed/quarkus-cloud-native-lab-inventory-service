package com.mohammed.inventory.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    @Column(name = "available_quantity", nullable = false)
    public int availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    public int reservedQuantity;

}
