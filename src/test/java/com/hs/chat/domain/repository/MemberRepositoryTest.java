package com.hs.chat.domain.repository;

import com.hs.chat.domain.model.user.member.Member;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        memberRepository.save(
                Member.builder()
                        .memberId("test")
                        .memberPassword("test")
                        .isDeleted(false)
                        .isLocked(false)
                        .build()
        );
    }

    @Test
    public void existTest() {
        assertTrue(memberRepository.existsMemberByMemberId("test"));
    }

}