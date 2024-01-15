package com.hs.chat.global.repository.jwt;

import com.hs.chat.global.model.jwt.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

//    Optional<JwtToken> findBy

}
