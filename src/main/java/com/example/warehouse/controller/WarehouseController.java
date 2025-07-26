package com.example.warehouse.controller;

import com.example.warehouse.entity.Warehouse;
import com.example.warehouse.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/warehouses")
public class WarehouseController {
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @GetMapping
    public String listWarehouses(Model model) {
        model.addAttribute("warehouses", warehouseRepository.findAll());
        return "warehouse/list";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("warehouse", new Warehouse());
        return "warehouse/form";
    }
    
    @PostMapping("/save")
    public String saveWarehouse(@ModelAttribute Warehouse warehouse) {
        warehouseRepository.save(warehouse);
        return "redirect:/warehouses";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Warehouse warehouse = warehouseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("仓库不存在"));
        model.addAttribute("warehouse", warehouse);
        return "warehouse/form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteWarehouse(@PathVariable Long id) {
        warehouseRepository.deleteById(id);
        return "redirect:/warehouses";
    }
}