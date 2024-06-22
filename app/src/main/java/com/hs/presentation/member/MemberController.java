package com.hs.presentation.member;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 컨트롤러
 * 로그인, 회원가입
 */
@RestController
public class MemberController {

    @PostMapping("/login")
    public String loginProcess() {
        return "login";
    }

    @PostMapping("/signup")
    public String signupProcess() {
        return "signup";
    }

}
