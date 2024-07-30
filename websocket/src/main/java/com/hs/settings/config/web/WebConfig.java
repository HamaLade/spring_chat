package com.hs.settings.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://spring-chat:8083", "https://spring-chat:8083"
                        , "http://spring-chat", "https://spring-chat"
                        , "http://localhost:8083", "https://localhost:8083"
                        , "http://localhost", "https://localhost"
                )
                .allowCredentials(true);
    }
}
