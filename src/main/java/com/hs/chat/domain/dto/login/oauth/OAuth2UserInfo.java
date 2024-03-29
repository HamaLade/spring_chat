package com.hs.chat.domain.dto.login.oauth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;

@Getter
@Setter
@NoArgsConstructor
public class OAuth2UserInfo {

    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private AuthenticationProvider provider;

    public OAuth2UserInfo(String id, String name, String email, String imageUrl, AuthenticationProvider provider) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.provider = provider;
    }
}
