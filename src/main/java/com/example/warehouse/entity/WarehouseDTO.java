package com.example.warehouse.entity;

public class WarehouseDTO {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    
    // Constructors
    public WarehouseDTO() {}
    
    public WarehouseDTO(Warehouse warehouse) {
        this.id = warehouse.getId();
        this.name = warehouse.getName();
        this.location = warehouse.getLocation();
        this.capacity = warehouse.getCapacity();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
