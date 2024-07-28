package com.hs.setting.config.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RateLimiterConfig {

    @Bean
    @Profile("!test")
    public RateLimiter productionRateLimiter() {
        return RateLimiter.create(0.25);
    }

    @Bean
    @Profile("test")
    public RateLimiter testRateLimiter() {
        return RateLimiter.create(10000); // 매우 높은 속도로 설정
    }
}
