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
                        "http://localhost:8083", "https://localhost:8083"
                        , "http://localhost", "https://localhost"
                        , "http://www.spring-chat.com:8083", "https://www.spring-chat.com:8083"
                        , "http://www.spring-chat.com", "https://www.spring-chat.com"
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
