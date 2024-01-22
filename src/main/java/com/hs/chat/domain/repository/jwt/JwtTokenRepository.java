package com.hs.chat.domain.repository.jwt;

import com.hs.chat.domain.model.jwt.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

//    Optional<JwtToken> findBy

}
