package com.hs.presentation.member;

import com.hs.application.member.dto.MemberAuthInfo;
import com.hs.application.member.service.MemberAuthService;
import com.hs.presentation.ApiPaths;
import com.hs.presentation.ResponseMessage;
import com.hs.presentation.error.Errors;
import com.hs.presentation.member.dto.MemberLoginRequestDto;
import com.hs.presentation.member.dto.MemberSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 컨트롤러
 * 로그인, 회원가입
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberAuthService memberAuthService;

    /**
     * 로그인 처리
     *
     * @param memberLoginRequestDto 로그인 요청 데이터
     * @return 로그인 실패 시 에러 메시지, 성공 시 null
     */
    @PostMapping(ApiPaths.MEMBER_LOGIN)
    public ResponseEntity<ResponseMessage> loginProcess(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {

        try {
            MemberAuthInfo memberAuthInfo = memberAuthService.login(memberLoginRequestDto.getLoginId(), memberLoginRequestDto.getPassword());
            return ResponseEntity.ok(new ResponseMessage("ok", memberAuthInfo));
        } catch (Exception e) {
            log.error("login failed", e);
            Errors error = Errors.LOGIN_FAILED;
            return ResponseMessage.errorResponseEntity(error, "로그인 실패", e);
        }
    }

    /**
     * 로그아웃 처리
     * @return 로그아웃 성공 시 로그아웃 페이지로 리다이렉트
     */
    @PostMapping(ApiPaths.MEMBER_LOGOUT)
    public String logoutProcess() {
        memberAuthService.logout();
        return "logout";
    }

    /**
     * 회원가입 페이지에서 Form 태그를 통해 Post 요청을 받아 처리
     * @param memberSignUpRequestDto 회원가입 폼에서 받은 데이터
     *                               loginId, nickname, password 필드를 가지고 있음
     * @return 회원가입 성공 시 인덱스 페이지로 리다이렉트
     */
    @PostMapping(ApiPaths.MEMBER_SIGNUP)
    public ResponseEntity<ResponseMessage> signupProcess(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto) {
        try {
            memberAuthService.signUp(
                    memberSignUpRequestDto.getLoginId(),
                    memberSignUpRequestDto.getNickname(),
                    memberSignUpRequestDto.getPassword()
            );
            return ResponseEntity.ok(new ResponseMessage("ok", null));
        } catch (Exception e) {
            log.error("signup failed", e);
            Errors error = Errors.SIGNUP_FAILED;
            return ResponseMessage.errorResponseEntity(error, "회원가입 실패", e);
        }
    }

    /**
     * 회원 탈퇴
     * @return 탈퇴 성공 시 인덱스 페이지로 리다이렉트
     */
    @PostMapping(ApiPaths.MEMBER_WITHDRAW)
    public String withdrawProcess() {
//        memberAuthService.withdraw();
        return "redirect:/home";
    }


}
