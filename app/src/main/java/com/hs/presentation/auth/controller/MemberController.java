package com.hs.presentation.auth.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.hs.application.member.dto.MemberAuthInfo;
import com.hs.application.member.service.MemberAuthService;
import com.hs.presentation.ApiPaths;
import com.hs.presentation.ResponseMessage;
import com.hs.presentation.auth.dto.MemberLoginRequestDto;
import com.hs.presentation.auth.dto.MemberSearchRequestDto;
import com.hs.presentation.auth.dto.MemberSignUpRequestDto;
import com.hs.presentation.error.Errors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 회원 컨트롤러
 * 로그인, 회원가입
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final RateLimiter rateLimiter = RateLimiter.create(0.25);
    private final MemberAuthService memberAuthService;

    /**
     * 로그인 처리
     *
     * @param memberLoginRequestDto 로그인 요청 데이터
     * @return 로그인 실패 시 에러 메시지, 성공 시 null
     */
    @ResponseBody
    @PostMapping(ApiPaths.MEMBER_LOGIN)
    public ResponseEntity<ResponseMessage> loginProcess(@RequestBody @Validated MemberLoginRequestDto memberLoginRequestDto) {

        MemberAuthInfo memberAuthInfo = memberAuthService.login(memberLoginRequestDto.getLoginId(), memberLoginRequestDto.getPassword());
        return ResponseEntity.ok(new ResponseMessage("ok", memberAuthInfo));
    }

    /**
     * 로그아웃 처리
     *
     * @return 로그아웃 성공 시 로그아웃 페이지로 리다이렉트
     */
    @ResponseBody
    @PostMapping(ApiPaths.MEMBER_LOGOUT)
    public String logoutProcess() {
        memberAuthService.logout();
        return "logout";
    }

    /**
     * 회원가입 페이지에서 Form 태그를 통해 Post 요청을 받아 처리
     *
     * @param memberSignUpRequestDto 회원가입 폼에서 받은 데이터
     *                               loginId, nickname, password 필드를 가지고 있음
     * @return 회원가입 성공 시 인덱스 페이지로 리다이렉트
     */
    @ResponseBody
    @PostMapping(ApiPaths.MEMBER_SIGNUP)
    public ResponseEntity<ResponseMessage> signupProcess(@RequestBody @Validated MemberSignUpRequestDto memberSignUpRequestDto) {

        if (!rateLimiter.tryAcquire()) {
            Errors tooManyRequest = Errors.TOO_MANY_REQUEST;
            return ResponseMessage.errorResponseEntity(
                    tooManyRequest
                    , tooManyRequest.getDefaultErrorMessage()
            );
        }

        memberAuthService.signUp(
                memberSignUpRequestDto.getLoginId(),
                memberSignUpRequestDto.getNickname(),
                memberSignUpRequestDto.getPassword()
        );
        return ResponseEntity.ok(new ResponseMessage("ok", null));
    }

    /**
     * 회원 탈퇴
     *
     * @return 탈퇴 성공 시 인덱스 페이지로 리다이렉트
     */
    @ResponseBody
    @DeleteMapping(ApiPaths.MEMBER_WITHDRAW)
    public ResponseEntity<ResponseMessage> withdrawProcess() {

        memberAuthService.withdraw();
        memberAuthService.logout();
        return ResponseEntity.ok(new ResponseMessage("ok", null));
    }

    @ResponseBody
    @PostMapping(ApiPaths.MEMBER_TOKEN_AUTHORIZE)
    public ResponseEntity<String> tokenAuthorize() {

        UserDetails authorization = memberAuthService.authorization();
        return ResponseEntity.ok(authorization.getUsername());

    }

    @ResponseBody
    @PostMapping(ApiPaths.MEMBER_SEARCH)
    public ResponseEntity<ResponseMessage> searchMember(@RequestBody @Validated MemberSearchRequestDto memberSearchRequestDto) {

        List<String> memberList = memberAuthService.searchMember(memberSearchRequestDto.getMemberNickname());
        return ResponseEntity.ok(new ResponseMessage("ok", memberList));
    }

}
