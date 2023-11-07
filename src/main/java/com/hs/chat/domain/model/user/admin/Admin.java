package com.hs.chat.domain.model.user.admin;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Admin implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminSeq;

    private String adminId;

    private String adminPassword;

    private Boolean isDeleted;

    private Boolean isLocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return adminPassword;
    }

    @Override
    public String getUsername() {
        return adminId;
    }

    @Override
    public boolean isAccountNonExpired() { // 계정 만료 없음
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isLocked != null && isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 비밀번호 만료 없음
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isDeleted != null && !isDeleted;
    }
}
