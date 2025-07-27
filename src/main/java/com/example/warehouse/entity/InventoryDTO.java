package com.example.warehouse.entity;

import java.time.LocalDateTime;

public class InventoryDTO {
    private Long id;
    private ProductDTO product;
    private WarehouseDTO warehouse;
    private Integer quantity;
    private LocalDateTime lastUpdated;
    
    // Constructors
    public InventoryDTO() {}
    
    public InventoryDTO(Inventory inventory) {
        this.id = inventory.getId();
        this.product = new ProductDTO(inventory.getProduct());
        this.warehouse = new WarehouseDTO(inventory.getWarehouse());
        this.quantity = inventory.getQuantity();
        this.lastUpdated = inventory.getLastUpdated();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public WarehouseDTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseDTO warehouse) {
        this.warehouse = warehouse;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
