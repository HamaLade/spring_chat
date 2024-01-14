package com.hs.chat.global.controller;

import com.hs.chat.domain.model.chat.room.ChatRoom;
import com.hs.chat.domain.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/")
    public String healthCheck(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<ChatRoom> publicChatRooms = null;
        List<ChatRoom> privateChatRooms = null;

        publicChatRooms = chatRoomService.availablePublicChatRooms();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            authentication.getPrincipal();
            authentication.getDetails();
            authentication.getCredentials();
        }

        if (authentication.isAuthenticated()) {
            authentication.getPrincipal();
            model.addAttribute("privateChatRooms", privateChatRooms);
        }

        model.addAttribute("publicChatRooms", publicChatRooms);

        return "index";
    }

}
