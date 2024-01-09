package com.hs.chat.domain.controller.login.chatroom;

import com.hs.chat.domain.dto.login.oauth.chatroom.ChatRoomCreateRequestDto;
import com.hs.chat.domain.model.chat.room.ChatRoom;
import com.hs.chat.domain.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatService chatService;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatService.findAllRoom();
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestBody ChatRoomCreateRequestDto chatRoomCreateRequestDto) {
        return chatService.createRoom(
                chatRoomCreateRequestDto.getRoomName()
                , chatRoomCreateRequestDto.getRoomPassword()
                , chatRoomCreateRequestDto.getIsPublic()
        );
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable Long roomId) {
        return chatService.findById(roomId);
    }
}
