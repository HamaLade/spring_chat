package com.hs.setting.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:8083", "https://localhost:8083"
                        , "http://localhost", "https://localhost"
                        , "http://www.spring-chat:8083", "https://www.spring-chat:8083"
                        , "http://www.spring-chat", "https://www.spring-chat"
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(600);
    }
}
