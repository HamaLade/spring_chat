package com.hs.chat.global.config;

import com.hs.chat.global.properties.Oauth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({Oauth2Properties.class})
@RequiredArgsConstructor
public class OauthConfig {

    private final Oauth2Properties oauth2Properties;

}
