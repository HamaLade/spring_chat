package com.hs.setting.config.jwt;

import com.hs.setting.utils.jwt.MemberJwtProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberJwtConfig {

    private final String accessTokenSecret;
    private final String refreshTokenSecret;
    private final long accessTokenExpiredTime;
    private final long refreshTokenExpiredTime;

    public MemberJwtConfig(
            @Value("${jwt.member.access.secret}") String accessTokenSecret,
            @Value("${jwt.member.refresh.secret}") String refreshTokenSecret,
            @Value("${jwt.member.access.exp}")long accessTokenExpiredTime,
            @Value("${jwt.member.refresh.exp}")long refreshTokenExpiredTime
    ) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.accessTokenExpiredTime = accessTokenExpiredTime;
        this.refreshTokenExpiredTime = refreshTokenExpiredTime;
    }

    @Bean
    public MemberJwtProperties jwtProperties() {
        return new MemberJwtProperties(
                accessTokenSecret,
                refreshTokenSecret,
                accessTokenExpiredTime,
                refreshTokenExpiredTime
        );
    }



}
