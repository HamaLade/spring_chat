package com.hs.chat.global.service.oauth;

import com.hs.chat.domain.model.user.member.Member;
import com.hs.chat.domain.service.login.oauth2.MemberService;
import com.hs.chat.global.common.LoginResponse;
import com.hs.chat.global.common.oauth.OauthProvider;
import com.hs.chat.global.common.oauth.UserProfile;
import com.hs.chat.global.dto.OauthTokenResponse;
import com.hs.chat.global.enums.OauthAttributes;
import com.hs.chat.global.enums.OauthAuthorize;
import com.hs.chat.global.repository.InMemoryProviderRepository;
import com.hs.chat.global.service.jwt.TokenProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Service
public class OauthService {

    private final InMemoryProviderRepository inMemoryProviderRepository;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    public OauthService(InMemoryProviderRepository inMemoryProviderRepository, MemberService memberService, TokenProvider tokenProvider) {
        this.inMemoryProviderRepository = inMemoryProviderRepository;
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponse login(String providerName, String code) {

        OauthProvider provider = inMemoryProviderRepository.findByProviderName(providerName);

        OauthTokenResponse tokenResponse = getToken(code, provider);

        UserProfile userProfile = getUserProfile(providerName, tokenResponse, provider);

        Member member = memberService.save(userProfile.toMember());

        LoginResponse loginResponse = LoginResponse.builder()
                .id(member.getMemberSeq())
                .name(member.getName())
                .email(member.getEmail())
                .imageUrl(member.getImageUrl())
                .userType(member.getUserType())
                .tokenType("Bearer")
                .accessToken(tokenProvider.generateToken(member, Duration.ofMinutes(15), false))
                .refreshToken(tokenProvider.generateToken(member, Duration.ofDays(1), true))
                .build();

        return loginResponse;
    }

    private UserProfile getUserProfile(String providerName, OauthTokenResponse tokenResponse, OauthProvider provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        return OauthAttributes.extract(providerName, userAttributes);
    }

    // OAuth 서버에서 유저 정보 map으로 가져오기
    private Map<String, Object> getUserAttributes(OauthProvider provider, OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getUserInfoUrl())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    private OauthTokenResponse getToken(String code, OauthProvider provider) {
        return WebClient.create()
                .post()
                .uri(provider.getTokenUrl())
                .headers(header -> {
                    header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code, OauthProvider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUrl());
        return formData;
    }

    public String getAuthorizeUrl(String providerName) {
        OauthProvider provider = inMemoryProviderRepository.findByProviderName(providerName);
        return requestAuthorizeUrl(providerName, provider);
    }

    private String requestAuthorizeUrl(String providerName, OauthProvider provider) {
        return OauthAuthorize.of(providerName).getAuthorizeUrl() + "?client_id=" + provider.getClientId() + "&redirect_uri=" + "http://localhost:8080/login/oauth/" + providerName + "&response_type=code";
    }

}