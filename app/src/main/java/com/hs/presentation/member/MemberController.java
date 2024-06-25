package com.hs.presentation.member;

import com.hs.application.member.service.MemberAuthService;
import com.hs.presentation.member.dto.MemberLoginRequestDto;
import com.hs.presentation.member.dto.MemberSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 컨트롤러
 * 로그인, 회원가입
 */
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/login")
    public String loginProcess(MemberLoginRequestDto memberLoginRequestDto) {

        memberAuthService.login(memberLoginRequestDto.getLoginId(), memberLoginRequestDto.getPassword());

        return "login";
    }

    /**
     * 회원가입 페이지에서 Form 태그를 통해 Post 요청을 받아 처리
     * @param memberSignUpRequestDto 회원가입 폼에서 받은 데이터
     *                               loginId, nickname, password 필드를 가지고 있음
     * @return
     */
    @PostMapping("/signup")
    public String signupProcess(@ModelAttribute MemberSignUpRequestDto memberSignUpRequestDto) {
        memberAuthService.signUp(
                memberSignUpRequestDto.getLoginId(),
                memberSignUpRequestDto.getNickname(),
                memberSignUpRequestDto.getPassword()
        );
        return "redirect:/login";
    }


}
