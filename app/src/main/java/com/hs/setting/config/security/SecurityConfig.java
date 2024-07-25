package com.hs.setting.config.security;

import com.hs.application.member.service.MemberAuthService;
import com.hs.setting.utils.jwt.UserAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final MemberAuthService memberAuthService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        (sessionManagement) ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        (authorizeRequest) -> // 로그인 빼고 전부 접근 불가
                                authorizeRequest
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
