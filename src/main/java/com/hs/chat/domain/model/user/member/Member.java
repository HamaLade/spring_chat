package com.hs.chat.domain.model.user.member;

import com.hs.chat.domain.model.user.enums.SocialType;
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

    private Boolean isSocial;

    private SocialType socialType;

    private String socialId;

    private String memberPassword;

    private Boolean isDeleted;

    private Boolean isLocked;

    private UserType userType;

    @Builder
    public Member(
            String memberId
            , SocialType socialType
            , String socialId
            , String memberPassword
            , Boolean isDeleted
            , Boolean isLocked
    ) {
        this.memberId = memberId;
        this.socialType = socialType;
        this.socialId = socialId;
        this.memberPassword = memberPassword;
        this.isDeleted = isDeleted;
        this.isLocked = isLocked;
        this.isSocial = false;
        this.userType = UserType.USER;
    }

    public Member(SocialType socialType, String socialId) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.isSocial = true;
        this.isDeleted = false;
        this.isLocked = false;
        this.userType = UserType.USER;
    }

    public User toUser() {
        return new User(String.valueOf(memberSeq), "", getAuthorities());
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
        return memberId == null ? socialId : memberId;
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
