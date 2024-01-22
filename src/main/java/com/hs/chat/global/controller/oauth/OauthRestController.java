package com.hs.chat.global.controller.oauth;

import com.hs.chat.global.common.LoginResponse;
import com.hs.chat.global.service.oauth.OauthService;
import com.hs.chat.global.util.HttpServletUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OauthRestController {

    private final OauthService oauthService;

    public OauthRestController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/login/oauth/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code) {
        LoginResponse loginResponse = oauthService.login(provider, code);
        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping("/login/oauth/authorize/{provider}")
    public ResponseEntity<String> authorize(@PathVariable String provider) throws IOException {
        String authorizeUrl = oauthService.getAuthorizeUrl(provider);
        HttpServletUtil.getHttpServletResponse().sendRedirect(authorizeUrl);
        return ResponseEntity.status(302).body(authorizeUrl);
    }

}
