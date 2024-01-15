package com.hs.chat.domain.service.login.oauth2;

import com.hs.chat.domain.model.user.enums.SocialType;
import com.hs.chat.domain.model.user.member.Member;
import com.hs.chat.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public boolean isExistEmail(String memberId) {
        return memberRepository.existsMemberByMemberId(memberId);
    }

    public boolean isExistSocialId(SocialType socialType, String socialId) {
        return memberRepository.existsMemberBySocialId(socialType, socialId);
    }

    public Member findBySocialId(SocialType socialType, String socialId) {
        return memberRepository.findBySocialId(socialType, socialId).orElse(null);
    }

}
