package com.hs.chat.domain.model.user.member;

import com.hs.chat.domain.model.user.enums.SocialType;
import com.hs.chat.global.enums.UserType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

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
}
