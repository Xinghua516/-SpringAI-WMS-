package com.example.warehouse.controller;

import com.example.warehouse.entity.Inventory;
import com.example.warehouse.entity.Warehouse;
import com.example.warehouse.repository.InventoryRepository;
import com.example.warehouse.repository.WarehouseRepository;
import com.example.warehouse.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
    
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
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Inventory> inventoryPage = inventoryService.getAllInventory(pageable);
        List<Warehouse> warehouses = warehouseRepository.findAll();
        
        model.addAttribute("inventoryList", inventoryPage.getContent());
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", inventoryPage.getTotalPages());
        model.addAttribute("totalItems", inventoryPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "inventory/list";
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    public String getInventoryByWarehouse(
            @PathVariable Long warehouseId, 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "product.name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Inventory> inventoryPage = inventoryService.getInventoryByWarehouse(warehouseId, pageable);
        List<Warehouse> warehouses = warehouseRepository.findAll();
        
        model.addAttribute("inventoryList", inventoryPage.getContent());
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", inventoryPage.getTotalPages());
        model.addAttribute("totalItems", inventoryPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("warehouseId", warehouseId);
        return "inventory/list";
    }
}