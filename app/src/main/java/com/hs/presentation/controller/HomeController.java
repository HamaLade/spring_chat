package com.hs.presentation.controller;

import com.hs.presentation.ApiPaths;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * index 화면 컨트롤러
 */
@Controller
public class HomeController {

    /**
     * index 화면
     * 인증 여부에 따라 로그인 혹은 로그아웃 버튼이 보여짐
     * @return home.html
     */
    @GetMapping(ApiPaths.HOME)
    public String home(Model model) {
        model.addAttribute("loginPage", ApiPaths.MEMBER_LOGIN);
        model.addAttribute("signupPage", ApiPaths.MEMBER_SIGNUP);
        return "home";
    }

    /**
     * 로그인 화면
     * 로그인 성공 시 home 화면으로 이동
     * @return login.html
     */
    @GetMapping(ApiPaths.MEMBER_LOGIN)
    public String login() {
        return "login";
    }

    /**
     * 회원가입 화면
     * 회원가입 성공 시 home 화면으로 이동
     * @return signup.html
     */
    @GetMapping(ApiPaths.MEMBER_SIGNUP)
    public String signup() {
        return "signup";
    }

    /**
     * 서버 상태 체크
     * @return alive
     */
    @ResponseBody
    @PostMapping("/")
    public String healthCheck() {
        // TODO: 단시간에 너무 많은 요청이 들어오지 않도록 제한을 둘 필요가 있음
        return "alive";
    }

}
