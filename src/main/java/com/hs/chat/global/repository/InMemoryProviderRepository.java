package com.hs.chat.global.repository;

import com.hs.chat.global.common.oauth.OauthProvider;

import java.util.HashMap;
import java.util.Map;

public class InMemoryProviderRepository {

    private final Map<String, OauthProvider> oauthProviders;

    public InMemoryProviderRepository(Map<String, OauthProvider> oauthProviders) {
        this.oauthProviders = new HashMap<>(oauthProviders);
    }

    public OauthProvider findByProviderName(String name) {
        return oauthProviders.get(name);
    }

}
