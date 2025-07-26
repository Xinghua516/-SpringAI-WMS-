package com.example.warehouse.controller;

import com.example.warehouse.config.SpringAIProperties;
import com.example.warehouse.service.ai.AISQLAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    
    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);
    
    @Autowired
    private SpringAIProperties springAIProperties;
    
    @Autowired
    private AISQLAssistantService aiSQLAssistantService;
    
    @GetMapping
    public String settingsForm(Model model) {
        model.addAttribute("settings", springAIProperties);
        return "settings/form";
    }
    
    @PostMapping("/save")
    public String saveSettings(SpringAIProperties settings, RedirectAttributes redirectAttributes) {
        springAIProperties.setBaseUrl(settings.getBaseUrl());
        springAIProperties.setApiKey(settings.getApiKey());
        springAIProperties.getChat().setModel(settings.getChat().getModel());
        
        // 保存到配置文件
        try {
            springAIProperties.saveToFile();
            redirectAttributes.addFlashAttribute("message", "设置已保存成功并立即生效！配置已持久化保存。");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (IOException e) {
            logger.error("保存配置文件时出错", e);
            redirectAttributes.addFlashAttribute("message", "设置已保存到内存，但保存到配置文件时出错: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
        }
        
        // 刷新AI助手服务中的ChatClient
        aiSQLAssistantService.refreshChatClient();
        
        return "redirect:/settings";
    }
}
