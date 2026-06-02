package com.mycompany.carmotor.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sirve las imágenes subidas desde el sistema de archivos
        String uploadPath = Paths.get("src/main/resources/static/img/uploads")
                .toAbsolutePath()
                .toUri()
                .toString();

        registry.addResourceHandler("/img/uploads/**")
                .addResourceLocations(uploadPath + "/");
    }
}