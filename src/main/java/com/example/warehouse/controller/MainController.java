package com.example.warehouse.controller;

import com.example.warehouse.entity.Admin;
import com.example.warehouse.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private StockInRepository stockInRepository;
    
    @Autowired
    private StockOutRepository stockOutRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @GetMapping("/")
    public String dashboard(Model model, HttpSession session) {
        // 检查是否已登录
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }
        
        long productCount = productRepository.count();
        long warehouseCount = warehouseRepository.count();
        long inCount = stockInRepository.count();
        long outCount = stockOutRepository.count();
        
        model.addAttribute("productCount", productCount);
        model.addAttribute("warehouseCount", warehouseCount);
        model.addAttribute("inCount", inCount);
        model.addAttribute("outCount", outCount);
        model.addAttribute("recentStockIns", stockInRepository.findAll());
        model.addAttribute("lowInventory", inventoryRepository.findAll());
        
        return "dashboard";
    }
    
    @GetMapping("/login")
    public String loginRedirect(HttpSession session) {
        // 如果已经登录，重定向到主页
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin != null) {
            return "redirect:/";
        }
        return "redirect:/admin/login";
    }
}