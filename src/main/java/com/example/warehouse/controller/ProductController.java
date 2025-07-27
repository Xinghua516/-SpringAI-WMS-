package com.example.warehouse.controller;

import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductDTO;
import com.example.warehouse.exception.BusinessLogicException;
import com.example.warehouse.exception.ResourceNotFoundException;
import com.example.warehouse.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping
    public String listProducts(Model model) {
        List<ProductDTO> products = productRepository.findAll().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        logger.info("成功获取商品列表，共 {} 条记录", products.size());
        return "product/list";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        logger.debug("进入商品创建表单页面");
        return "product/form";
    }
    
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute ProductDTO productDTO) {
        try {
            Product product;
            if (productDTO.getId() != null) {
                product = productRepository.findById(productDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("商品未找到，ID: " + productDTO.getId()));
                logger.debug("更新现有商品，ID: {}", productDTO.getId());
            } else {
                product = new Product();
                product.setCreatedAt(LocalDateTime.now());
                logger.debug("创建新商品");
            }
            
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setCategory(productDTO.getCategory());
            product.setUnitPrice(productDTO.getUnitPrice());
            product.setUnit(productDTO.getUnit());
            
            productRepository.save(product);
            logger.info("商品保存成功，ID: {}", product.getId());
            return "redirect:/products";
        } catch (ResourceNotFoundException e) {
            logger.warn("商品未找到: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("保存商品时出错", e);
            throw new BusinessLogicException("保存商品失败: " + e.getMessage(), e);
        }
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("无效的商品ID:" + id));
            model.addAttribute("product", new ProductDTO(product));
            logger.debug("进入商品编辑表单页面，商品ID: {}", id);
            return "product/form";
        } catch (ResourceNotFoundException e) {
            logger.warn("商品未找到: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("获取商品信息失败，商品ID: {}", id, e);
            throw new BusinessLogicException("获取商品信息失败: " + e.getMessage(), e);
        }
    }
    
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            if (!productRepository.existsById(id)) {
                throw new ResourceNotFoundException("商品未找到，ID: " + id);
            }
            productRepository.deleteById(id);
            logger.info("商品删除成功，ID: {}", id);
            return "redirect:/products";
        } catch (ResourceNotFoundException e) {
            logger.warn("商品未找到: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("删除商品失败，ID: {}", id, e);
            throw new BusinessLogicException("删除商品失败: " + e.getMessage(), e);
        }
    }
}