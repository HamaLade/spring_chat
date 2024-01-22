package com.hs.chat.domain.model.jwt;

import com.hs.chat.global.enums.UserType;
import com.hs.chat.global.util.HttpServletUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jwtTokenSeq;

    private String jwtToken;

    private Boolean isRefreshToken;

    private String ip;

    private UserType userType;

    private Boolean isOauth;

    private Long userSeq;

    @CreatedDate
    private LocalDateTime createdDate;

    public JwtToken(String jwtToken, UserType userType, Long userSeq, Boolean isOauth) {
        this.jwtToken = jwtToken;
        this.userType = userType;
        this.userSeq = userSeq;
        this.isRefreshToken = true;
        this.isOauth = isOauth;
        this.ip = HttpServletUtil.getClientIP();
    }

    public void update(String jwtToken, Boolean isOauth) {
        this.jwtToken = jwtToken;
        this.isOauth = isOauth;
        this.ip = HttpServletUtil.getClientIP();
    }

}
