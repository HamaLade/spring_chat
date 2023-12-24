package com.hs.chat.global.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    @GetMapping("/")
    public String healthCheck() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("principal = " + authentication);

        return "index";
    }

}
