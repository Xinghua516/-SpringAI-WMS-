package com.example.warehouse.service;

import com.example.warehouse.entity.*;
import com.example.warehouse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Transactional
    public void stockIn(StockIn stockIn) {
        // 更新库存
        Inventory inventory = inventoryRepository
            .findByProductAndWarehouse(stockIn.getProduct(), stockIn.getWarehouse())
            .orElseGet(() -> {
                Inventory newInventory = new Inventory();
                newInventory.setProduct(stockIn.getProduct());
                newInventory.setWarehouse(stockIn.getWarehouse());
                newInventory.setQuantity(0);
                return newInventory;
            });
        
        inventory.setQuantity(inventory.getQuantity() + stockIn.getQuantity());
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    @Transactional
    public void stockOut(StockOut stockOut) {
        Inventory inventory = inventoryRepository
            .findByProductAndWarehouse(stockOut.getProduct(), stockOut.getWarehouse())
            .orElseThrow(() -> new RuntimeException("库存不足"));
        
        if (inventory.getQuantity() < stockOut.getQuantity()) {
            throw new RuntimeException("库存不足");
        }
        
        inventory.setQuantity(inventory.getQuantity() - stockOut.getQuantity());
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
    
    public List<Inventory> getInventoryByWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new RuntimeException("仓库不存在"));
        return inventoryRepository.findByWarehouse(warehouse);
    }
    
    public Optional<Inventory> getInventoryByProductAndWarehouse(Product product, Warehouse warehouse) {
        return inventoryRepository.findByProductAndWarehouse(product, warehouse);
    }
    
    // 添加分页查询方法
    public Page<Inventory> getAllInventory(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }
    
    public Page<Inventory> getInventoryByWarehouse(Long warehouseId, Pageable pageable) {
        return inventoryRepository.findByWarehouseId(warehouseId, pageable);
    }
}