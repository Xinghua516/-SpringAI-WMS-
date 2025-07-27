package com.example.warehouse.repository;

import com.example.warehouse.entity.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockOutRepository extends JpaRepository<StockOut, Long> {
    List<StockOut> findByProductId(Long productId);
    List<StockOut> findByWarehouseId(Long warehouseId);
}