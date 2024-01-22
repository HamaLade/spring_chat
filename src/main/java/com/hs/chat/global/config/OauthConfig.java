package com.hs.chat.global.config;

import com.hs.chat.global.common.oauth.OauthAdapter;
import com.hs.chat.global.common.oauth.OauthProvider;
import com.hs.chat.global.properties.Oauth2Properties;
import com.hs.chat.global.repository.InMemoryProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties({Oauth2Properties.class})
public class OauthConfig {

    private final Oauth2Properties oauth2Properties;

    @Autowired
    public OauthConfig(Oauth2Properties oauth2Properties) {
        this.oauth2Properties = oauth2Properties;
    }

    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository() {
        Map<String, OauthProvider> providers = OauthAdapter.getOauthProviders(oauth2Properties);
        return new InMemoryProviderRepository(providers);
    }

}
