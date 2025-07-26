package com.example.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConfigurationProperties(prefix = "spring.ai.openai")
public class SpringAIProperties {
    
    private String baseUrl = "http://localhost:1234";
    private String apiKey = "lm-studio";
    private ChatOptions chat = new ChatOptions();
    
    public static class ChatOptions {
        private String model = "deepseek-coder-1.3b-instruct";
        
        // Getters and Setters
        public String getModel() {
            return model;
        }
        
        public void setModel(String model) {
            this.model = model;
        }
    }
    
    // Getters and Setters
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public ChatOptions getChat() {
        return chat;
    }
    
    public void setChat(ChatOptions chat) {
        this.chat = chat;
    }
    
    /**
     * 将当前配置保存到application.properties文件中
     */
    public void saveToFile() throws IOException {
        // 获取项目根目录
        Path projectRoot = Paths.get("").toAbsolutePath();
        Path propertiesFile = projectRoot.resolve("src/main/resources/application.properties");
        
        // 如果文件不存在，则创建
        if (!Files.exists(propertiesFile)) {
            Files.createDirectories(propertiesFile.getParent());
            Files.createFile(propertiesFile);
        }
        
        // 读取现有属性
        Properties props = new Properties();
        props.load(Files.newInputStream(propertiesFile));
        
        // 更新属性
        props.setProperty("spring.ai.openai.base-url", this.baseUrl);
        props.setProperty("spring.ai.openai.api-key", this.apiKey);
        props.setProperty("spring.ai.openai.chat.options.model", this.chat.getModel());
        
        // 也更新自定义属性（保持一致性）
        props.setProperty("app.ai.base-url", this.baseUrl);
        props.setProperty("app.ai.api-key", this.apiKey);
        props.setProperty("app.ai.model", this.chat.getModel());
        
        // 写入文件
        try (FileOutputStream fos = new FileOutputStream(propertiesFile.toFile())) {
            props.store(fos, "Updated AI Configuration");
        }
    }
}
