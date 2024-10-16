package com.nhom1.bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình để Spring Boot phục vụ ảnh từ thư mục ngoài D:/
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:D:/bookstore/images/")  // Thư mục chứa ảnh
                .setCachePeriod(0);  // Không cache để nhận diện ảnh mới ngay lập tức
    }
}
