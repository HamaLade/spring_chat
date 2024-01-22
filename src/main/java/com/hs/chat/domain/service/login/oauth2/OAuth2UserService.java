package com.hs.chat.domain.service.login.oauth2;

import com.hs.chat.domain.exception.oauth.OauthException;
import com.hs.chat.domain.model.user.enums.OauthType;
import com.hs.chat.domain.model.user.member.Member;
import com.hs.chat.global.service.jwt.TokenProvider;
import com.hs.chat.global.util.AuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OauthException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        List<GrantedAuthority> authorities = new ArrayList<>();

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.info("userNameAttributeName (유저 이름 속성) : {}", userNameAttributeName);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();
        OauthType socialType = OauthType.of(registrationId);
        String clientId = clientRegistration.getClientId();
        Member member = null;

        switch (socialType) {
            case GOOGLE:
                log.info("구글 로그인 요청");
                break;
            case NAVER:
                log.info("네이버 로그인 요청");
                break;
            case KAKAO:
                log.info("카카오 로그인 요청");
                member = kakaoOauthProcess(clientId);
                break;
        }

        // Member가 null이 아니면, 회원이므로 ROLE_USER 권한을 부여하여 SecurityContext에 저장
        if (member != null) {
            authorities = createAuthorityList(AuthorityUtils.getRoleName(member.getUserType()));
            String token = tokenProvider.generateToken(member.toUser(), Duration.ofHours(3));
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }

    private Member kakaoOauthProcess(String socialId) {

        if (memberService.isExistSocialId(OauthType.KAKAO, socialId)) {
            log.info("이미 가입된 회원입니다 제공사 : {}, 클라이언트 아이디 : {}", OauthType.KAKAO, "");
            return memberService.findBySocialId(OauthType.KAKAO, socialId);
        } else {
            log.info("회원가입을 진행합니다 제공사 : {}, 클라이언트 아이디 : {}", OauthType.KAKAO, "");
            return memberService.save(new Member(OauthType.KAKAO, socialId));
        }

    }

}

