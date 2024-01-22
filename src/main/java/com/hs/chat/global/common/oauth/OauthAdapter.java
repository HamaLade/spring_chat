package com.hs.chat.global.common.oauth;

import com.hs.chat.global.properties.Oauth2Properties;

import java.util.HashMap;
import java.util.Map;

public class OauthAdapter {

    private OauthAdapter() {}

    public static Map<String, OauthProvider> getOauthProviders(Oauth2Properties oauth2Properties) {
        Map<String, OauthProvider> oauthProviders = new HashMap<>();

        oauth2Properties.getUser().forEach((key, value) -> {
            oauthProviders.put(key, new OauthProvider(value, oauth2Properties.getProvider().get(key)));
        });

        return oauthProviders;
    }

}
