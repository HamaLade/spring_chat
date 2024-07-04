package com.hs.persistence;

import com.hs.persistence.entity.member.Member;
import com.hs.persistence.repository.memeber.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Slf4j
@DataJpaTest
@Sql({"/sql/test/schema.sql", "/sql/test/data.sql"})
@ActiveProfiles("test")
@DisplayName("MemberRepository 테스트")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("로그인 아이디로 회원 조회")
    void findByLoginIdTest() {

        String loginId = "test01";

        Member foundMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        Assertions.assertEquals(loginId, foundMember.getLoginId());
    }

    @Test
    @DisplayName("로그인 아이디로 회원 조회 실패")
    void findByLoginIdFailTest() {

        String loginId = "noneLoginId";

        Assertions.assertThrows(RuntimeException.class, () -> {
            memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new RuntimeException("회원 조회 실패"));
        });
    }

    @Test
    @DisplayName("닉네임으로 회원 조회")
    void findByNicknameTest() {

        String nickname = "test01";

        Member foundMember = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        Assertions.assertEquals(nickname, foundMember.getNickname());
    }

    @Test
    @DisplayName("닉네임으로 회원 조회 실패")
    void findByNicknameFailTest() {

        String nickname = "noneNickname";

        Assertions.assertThrows(RuntimeException.class, () -> {
            memberRepository.findByNickname(nickname)
                    .orElseThrow(() -> new RuntimeException("회원 조회 실패"));
        });
    }

    @Test
    @DisplayName("회원 저장")
    void saveTest() {

        Member member = new Member("savetest02", "savetest02", "savetest02");

        Member savedMember = memberRepository.save(member);

        Assertions.assertNotNull(savedMember.getId());
        Assertions.assertEquals(member.getLoginId(), savedMember.getLoginId());
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteTest() {

        String loginId = "delete_target";

        Member foundMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        memberRepository.delete(foundMember);

        Assertions.assertThrows(RuntimeException.class, () -> {
            memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new RuntimeException("회원 삭제 실패"));
        });
    }

}
