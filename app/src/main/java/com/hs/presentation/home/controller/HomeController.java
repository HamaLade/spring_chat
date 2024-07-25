package com.hs.presentation.home.controller;

import com.hs.application.room.dto.ChatRoomInfo;
import com.hs.application.room.service.RoomService;
import com.hs.presentation.ApiPaths;
import com.hs.setting.utils.member.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * index 화면 컨트롤러
 */
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final RoomService roomService;

    /**
     * index 화면
     * 인증 여부에 따라 로그인 혹은 로그아웃 버튼이 보여짐
     *
     * @return home.html
     */
    @GetMapping(ApiPaths.HOME)
    public String home(Model model) {

        // annonymous 인증이 아닐 때만 채팅방 목록을 보여줌
        if (MemberUtil.isNotAnnonyMousUser()) {
            List<ChatRoomInfo> joinedChatRooms =  roomService.getJoinedChatRoom(PageRequest.of(0, 30, Sort.Direction.DESC, "id")).getContent();
            model.addAttribute("joinedChatRooms", joinedChatRooms);
        }

        model.addAttribute("loginPage", ApiPaths.MEMBER_LOGIN);
        model.addAttribute("signupPage", ApiPaths.MEMBER_SIGNUP);
        return "home";
    }

    /**
     * 로그인 화면
     * 로그인 성공 시 home 화면으로 이동
     *
     * @return login.html
     */
    @GetMapping(ApiPaths.MEMBER_LOGIN)
    public String login() {
        return "login";
    }

    /**
     * 회원가입 화면
     * 회원가입 성공 시 home 화면으로 이동
     *
     * @return signup.html
     */
    @GetMapping(ApiPaths.MEMBER_SIGNUP)
    public String signup() {
        return "signup";
    }

    /**
     * 서버 상태 체크
     *
     * @return alive
     */
    @ResponseBody
    @PostMapping("/")
    public String healthCheck() {
        // TODO: 단시간에 너무 많은 요청이 들어오지 않도록 제한을 둘 필요가 있음
        return "alive";
    }

    /**
     * 채팅방 탐색 화면
     * 채팅방 탐색 화면으로 이동
     */
    @GetMapping(ApiPaths.CHAT_ROOM_ROOT_PATH)
    public String chatRoom() {
        return "chat-rooms";
    }

    /**
     * 채팅방 생성 화면
     * 채팅방 생성 화면으로 이동
     */
    @GetMapping(ApiPaths.CHAT_ROOM_CREATE)
    public String chatRoomCreate() {
        return "chat-room-create";
    }

}
