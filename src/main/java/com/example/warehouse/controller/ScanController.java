package com.example.warehouse.controller;

import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.StockIn;
import com.example.warehouse.entity.StockOut;
import com.example.warehouse.entity.Warehouse;
import com.example.warehouse.repository.ProductRepository;
import com.example.warehouse.repository.StockInRepository;
import com.example.warehouse.repository.StockOutRepository;
import com.example.warehouse.repository.WarehouseRepository;
import com.example.warehouse.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/scan")
public class ScanController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StockInRepository stockInRepository;
    
    @Autowired
    private StockOutRepository stockOutRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private InventoryService inventoryService;
    
    /**
     * 通过条形码查找商品信息
     */
    @GetMapping("/product/{barcode}")
    @ResponseBody
    public ResponseEntity<Product> getProductByBarcode(@PathVariable String barcode) {
        Product product = productRepository.findByBarcode(barcode);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 处理扫描入库请求
     */
    @PostMapping("/stock-in")
    @ResponseBody
    public ResponseEntity<String> handleStockIn(@RequestParam String barcode, 
                                               @RequestParam Long warehouseId,
                                               @RequestParam int quantity,
                                               @RequestParam String operator) {
        try {
            // 查找商品
            Product product = productRepository.findByBarcode(barcode);
            if (product == null) {
                return ResponseEntity.badRequest().body("未找到对应条形码的商品");
            }
            
            // 查找仓库
            Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
            if (warehouse == null) {
                return ResponseEntity.badRequest().body("未找到指定仓库");
            }
            
            // 创建入库记录
            StockIn stockIn = new StockIn();
            stockIn.setProduct(product);
            stockIn.setWarehouse(warehouse);
            stockIn.setQuantity(quantity);
            stockIn.setOperator(operator);
            stockIn.setInTime(LocalDateTime.now());
            stockIn.setNotes("扫码入库");
            
            // 保存入库记录
            stockInRepository.save(stockIn);
            
            // 更新库存
            inventoryService.stockIn(stockIn);
            
            return ResponseEntity.ok("入库操作成功完成");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("入库操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 处理扫描出库请求
     */
    @PostMapping("/stock-out")
    @ResponseBody
    public ResponseEntity<String> handleStockOut(@RequestParam String barcode,
                                                @RequestParam Long warehouseId,
                                                @RequestParam int quantity,
                                                @RequestParam String operator) {
        try {
            // 查找商品
            Product product = productRepository.findByBarcode(barcode);
            if (product == null) {
                return ResponseEntity.badRequest().body("未找到对应条形码的商品");
            }
            
            // 查找仓库
            Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
            if (warehouse == null) {
                return ResponseEntity.badRequest().body("未找到指定仓库");
            }
            
            // 创建出库记录
            StockOut stockOut = new StockOut();
            stockOut.setProduct(product);
            stockOut.setWarehouse(warehouse);
            stockOut.setQuantity(quantity);
            stockOut.setOperator(operator);
            stockOut.setOutTime(LocalDateTime.now());
            stockOut.setNotes("扫码出库");
            
            // 保存出库记录
            stockOutRepository.save(stockOut);
            
            // 更新库存
            inventoryService.stockOut(stockOut);
            
            return ResponseEntity.ok("出库操作成功完成");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("出库操作失败: " + e.getMessage());
        }
    }
}