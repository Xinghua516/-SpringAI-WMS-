package com.example.warehouse.controller;

import com.example.warehouse.entity.Admin;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.StockOut;
import com.example.warehouse.entity.Warehouse;
import com.example.warehouse.repository.ProductRepository;
import com.example.warehouse.repository.StockOutRepository;
import com.example.warehouse.repository.WarehouseRepository;
import com.example.warehouse.service.InventoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/stock/out")
public class StockOutController {
    
    @Autowired
    private StockOutRepository stockOutRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private InventoryService inventoryService;
    
    @GetMapping
    public String listStockOuts(Model model) {
        model.addAttribute("stockOuts", stockOutRepository.findAll());
        return "stock/out/list";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("stockOut", new StockOut());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("warehouses", warehouseRepository.findAll());
        return "stock/out/form";
    }
    
    @PostMapping("/save")
    public String saveStockOut(@ModelAttribute StockOut stockOut, 
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            // 获取当前登录的管理员
            Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
            if (loggedInAdmin != null) {
                stockOut.setOperator(loggedInAdmin.getName());
            } else {
                stockOut.setOperator("未知操作员");
            }
            
            stockOut.setOutTime(LocalDateTime.now());
            stockOutRepository.save(stockOut);
            // 更新库存
            inventoryService.stockOut(stockOut);
            
            // 添加成功消息
            redirectAttributes.addFlashAttribute("message", "出库操作成功完成！");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            
            // 重定向到库存页面，显示更新后的库存
            return "redirect:/inventory";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "出库操作失败：" + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/stock/out/create";
        }
    }
}