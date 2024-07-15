package com.hs.persistence.repository.memeber;

import com.hs.persistence.entity.member.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 회원 레포지토리
 */
@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByNickname(String nickname);

    List<Member> findAllByIdIn(List<Long> ids);

    // MemberId를 Key로 하는 Map<Long, Member>을 반환


}
