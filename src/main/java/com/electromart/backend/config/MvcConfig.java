package com.electromart.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "uploads" là thư mục bạn đã tạo trong FileStorageService
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
