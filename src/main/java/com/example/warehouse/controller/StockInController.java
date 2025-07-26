package com.example.warehouse.controller;

import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.StockIn;
import com.example.warehouse.entity.Warehouse;
import com.example.warehouse.repository.ProductRepository;
import com.example.warehouse.repository.StockInRepository;
import com.example.warehouse.repository.WarehouseRepository;
import com.example.warehouse.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/stock/in")
public class StockInController {

    @Autowired
    private StockInRepository stockInRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public String listStockIns(Model model) {
        model.addAttribute("stockIns", stockInRepository.findAll());
        return "stock/in/list";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("stockIn", new StockIn());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("warehouses", warehouseRepository.findAll());
        return "stock/in/form";
    }

    @PostMapping("/save")
    public String saveStockIn(@ModelAttribute StockIn stockIn, RedirectAttributes redirectAttributes) {
        stockIn.setInTime(LocalDateTime.now());
        stockInRepository.save(stockIn);
        // 更新库存
        inventoryService.stockIn(stockIn);

        // 添加成功消息
        redirectAttributes.addFlashAttribute("message", "入库操作成功完成！");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        // 重定向到库存页面，显示更新后的库存
        return "redirect:/inventory";
    }
}