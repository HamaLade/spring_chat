package com.hs.persistance.repository.memeber;

import com.hs.persistance.entity.member.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 회원 레포지토리
 */
@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    /**
     * 회원 아이디로 회원 정보 조회
     *
     * @param loginId 회원 아이디
     * @return 회원 정보
     */
    Optional<Member> findByLoginId(String loginId);

    /**
     * 닉네임으로 회원 정보 조회
     * @param nickname 닉네임
     * @return 회원 정보
     */
    Optional<Member> findByNickname(String nickname);



}
