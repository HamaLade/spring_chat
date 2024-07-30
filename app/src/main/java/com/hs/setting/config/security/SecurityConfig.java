package com.hs.setting.config.security;

import com.hs.application.member.service.MemberAuthService;
import com.hs.setting.utils.jwt.UserAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final MemberAuthService memberAuthService;

    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(List.of(
                    "http://localhost:8083", "https://localhost:8083"
                    , "http://localhost", "https://localhost"
                    , "http://www.spring-chat.com:8083", "https://www.spring-chat.com:8083"
                    , "http://www.spring-chat.com", "https://www.spring-chat.com"
            ));
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        (sessionManagement) ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        (authorizeRequest) -> // 로그인 빼고 전부 접근 불가
                                authorizeRequest
                                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                        .requestMatchers("/", "/favicon.ico", "/members/login", "/members/signup", "members/logout", "/boards/**").permitAll()
                                        .requestMatchers("/chat/**").hasAnyRole("USER", "ADMIN")
                                        .anyRequest().authenticated()
                )
                .addFilterBefore(new UserAuthenticationFilter(memberAuthService), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
        ;
        return http.build();
    }

}
