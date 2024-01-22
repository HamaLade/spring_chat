package com.hs.chat.global.common.jwt;


import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtProperties {
    private String issuer;
    private Key secretKey;

    public String getIssuer() {
        if (issuer == null)
            return "gkntjriqwe@gmail.com";
        return issuer;
    }

    public Key getSecretKey() {
        if (secretKey == null)
            secretKey =  Keys.hmacShaKeyFor(Decoders.BASE64.decode("hhhhhhhhhhsssssssssskkkkkkkkkk12hhhhhhhhhhsssssssssskkkkkkkkkk12"));
        return secretKey;
    }
}
