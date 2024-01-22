package com.hs.chat.domain.repository.user;

import com.hs.chat.domain.model.user.enums.OauthType;
import com.hs.chat.domain.model.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByMemberId(String memberId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE m.oauthType = :oauthType AND m.oauthId = :oauthId")
    boolean existsMemberByOauthId(OauthType oauthType, String oauthId);

    @Query("SELECT m FROM Member m WHERE m.oauthType = :oauthType AND m.oauthId = :oauthId")
    Optional<Member> findByOauthId(OauthType oauthType, String oauthId);

}
