package com.example.warehouse.repository;

import com.example.warehouse.entity.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockInRepository extends JpaRepository<StockIn, Long> {
    List<StockIn> findByProductId(Long productId);
    List<StockIn> findByWarehouseId(Long warehouseId);
}