package com.example.warehouse.repository;

import com.example.warehouse.entity.Inventory;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductAndWarehouse(Product product, Warehouse warehouse);
    List<Inventory> findByWarehouse(Warehouse warehouse);
    List<Inventory> findByWarehouseId(Long warehouseId);
    
    // 添加分页查询方法
    Page<Inventory> findAll(Pageable pageable);
    Page<Inventory> findByWarehouseId(Long warehouseId, Pageable pageable);
}