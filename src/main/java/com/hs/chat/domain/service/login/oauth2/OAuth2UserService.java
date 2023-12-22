package com.hs.chat.domain.service.login.oauth2;

import com.hs.chat.domain.model.user.enums.SocialType;
import com.hs.chat.domain.model.user.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Role generate
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.info("userNameAttributeName (유저 이름 속성) : {}", userNameAttributeName);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();
        SocialType socialType = SocialType.of(registrationId);
        String clientId = clientRegistration.getClientId();

        if (memberService.isExistSocialId(socialType, clientId)) {
            log.info("이미 가입된 회원입니다 제공사 : {}, 클라이언트 아이디 : {}", socialType, clientId);
        } else {
            log.info("회원가입을 진행합니다 제공사 : {}, 클라이언트 아이디 : {}", socialType, clientId);
            memberService.save(new Member(socialType, clientId));
        }

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }
}

