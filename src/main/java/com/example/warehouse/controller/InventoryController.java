package com.example.warehouse.controller;

import com.example.warehouse.entity.Inventory;
import com.example.warehouse.entity.InventoryDTO;
import com.example.warehouse.entity.WarehouseDTO;
import com.example.warehouse.exception.BusinessLogicException;
import com.example.warehouse.exception.ResourceNotFoundException;
import com.example.warehouse.repository.InventoryRepository;
import com.example.warehouse.repository.WarehouseRepository;
import com.example.warehouse.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private InventoryService inventoryService;
    
    @GetMapping
    public String listInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "product.name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Inventory> inventoryPage = inventoryService.getAllInventory(pageable);
            
            // Convert to DTOs
            List<InventoryDTO> inventoryDTOs = inventoryPage.getContent().stream()
                    .map(InventoryDTO::new)
                    .collect(Collectors.toList());
            
            Page<InventoryDTO> inventoryDTOPage = new PageImpl<>(inventoryDTOs, pageable, inventoryPage.getTotalElements());
            
            List<WarehouseDTO> warehouseDTOs = warehouseRepository.findAll().stream()
                    .map(WarehouseDTO::new)
                    .collect(Collectors.toList());
            
            model.addAttribute("inventoryList", inventoryDTOPage.getContent());
            model.addAttribute("warehouses", warehouseDTOs);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", inventoryDTOPage.getTotalPages());
            model.addAttribute("totalItems", inventoryDTOPage.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            
            logger.info("成功获取库存列表，页码: {}, 每页数量: {}, 总记录数: {}", page, size, inventoryDTOPage.getTotalElements());
            return "inventory/list";
        } catch (Exception e) {
            logger.error("获取库存列表失败", e);
            throw new BusinessLogicException("获取库存列表失败: " + e.getMessage(), e);
        }
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    public String getInventoryByWarehouse(
            @PathVariable Long warehouseId, 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "product.name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Inventory> inventoryPage = inventoryService.getInventoryByWarehouse(warehouseId, pageable);
            
            // Convert to DTOs
            List<InventoryDTO> inventoryDTOs = inventoryPage.getContent().stream()
                    .map(InventoryDTO::new)
                    .collect(Collectors.toList());
            
            Page<InventoryDTO> inventoryDTOPage = new PageImpl<>(inventoryDTOs, pageable, inventoryPage.getTotalElements());
            
            List<WarehouseDTO> warehouseDTOs = warehouseRepository.findAll().stream()
                    .map(WarehouseDTO::new)
                    .collect(Collectors.toList());
            
            model.addAttribute("inventoryList", inventoryDTOPage.getContent());
            model.addAttribute("warehouses", warehouseDTOs);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", inventoryDTOPage.getTotalPages());
            model.addAttribute("totalItems", inventoryDTOPage.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("warehouseId", warehouseId);
            
            logger.info("成功获取仓库库存列表，仓库ID: {}, 页码: {}, 每页数量: {}, 总记录数: {}", 
                       warehouseId, page, size, inventoryDTOPage.getTotalElements());
            return "inventory/list";
        } catch (Exception e) {
            logger.error("获取仓库库存列表失败，仓库ID: {}", warehouseId, e);
            throw new BusinessLogicException("获取仓库库存列表失败: " + e.getMessage(), e);
        }
    }
}