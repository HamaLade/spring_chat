package com.hs.chat.domain.model.user.member;

import com.hs.chat.domain.model.user.enums.OauthType;
import com.hs.chat.global.enums.UserType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member implements UserDetails, OAuth2User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq;

    private String memberId;

    private Boolean isOauth;

    private OauthType oauthType;

    private String oauthId;

    private String email;

    private String name;

    private String nickname;

    private String imageUrl;

    private String memberPassword;

    private Boolean isDeleted;

    private Boolean isLocked;

    private UserType userType;

    @Builder
    public Member(
            String memberId
            , OauthType oauthType
            , String oauthId
            , String email
            , String name
            , String nickname
            , String imageUrl
            , String memberPassword
            , Boolean isDeleted
            , Boolean isLocked
    ) {
        this.memberId = memberId;
        this.oauthType = oauthType;
        this.oauthId = oauthId;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.memberPassword = memberPassword;
        this.isDeleted = isDeleted;
        this.isLocked = isLocked;
        this.isOauth = false;
        this.userType = UserType.USER;
    }

    public Member(OauthType oauthType, String oauthId) {
        this.oauthType = oauthType;
        this.oauthId = oauthId;
        this.isOauth = true;
        this.isDeleted = false;
        this.isLocked = false;
        this.userType = UserType.USER;
    }

    @Override
    public Map<String, Object> getAttributes() {
return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(UserType.USER.name());
        return org.springframework.security.core.authority.AuthorityUtils.createAuthorityList(authority.getAuthority());
    }

    @Override
    public String getPassword() {
        return memberPassword;
    }

    @Override
    public String getUsername() {
        return String.valueOf(memberSeq);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isDeleted != null && !isDeleted;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isLocked != null && !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isDeleted != null && !isDeleted;
    }

    @Override
    public String getName() {
        return null;
    }
}
