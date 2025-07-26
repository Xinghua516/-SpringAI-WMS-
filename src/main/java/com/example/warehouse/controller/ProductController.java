package com.example.warehouse.controller;

import com.example.warehouse.entity.Product;
import com.example.warehouse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "product/list";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/form";
    }
    
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
        return "redirect:/products";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("产品不存在"));
        model.addAttribute("product", product);
        return "product/form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }
}