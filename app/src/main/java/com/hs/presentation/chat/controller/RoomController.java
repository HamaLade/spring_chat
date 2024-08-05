package com.hs.presentation.chat.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.hs.application.member.service.MemberAuthService;
import com.hs.application.room.dto.ChatRoomDetailInfo;
import com.hs.application.room.dto.ChatRoomInfo;
import com.hs.application.room.service.RoomService;
import com.hs.presentation.ApiPaths;
import com.hs.presentation.ResponseMessage;
import com.hs.presentation.chat.dto.CreateRoomRequestDto;
import com.hs.presentation.chat.dto.InviteMemberRequestDto;
import com.hs.presentation.chat.dto.PreviousChatMessageRequestDto;
import com.hs.presentation.error.Errors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 채팅방 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class RoomController {

    private final RateLimiter rateLimiter;
    private final RoomService roomService;
    private final MemberAuthService memberAuthService;

    @ResponseBody
    @PostMapping(ApiPaths.CHAT_ROOM_PUBLIC)
    public ResponseEntity<ResponseMessage> getPublicRooms(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ChatRoomInfo> publicRoomsPage = roomService.getPublicRoomsPage(pageable);

        return ResponseEntity.ok(new ResponseMessage("public rooms", publicRoomsPage));
    }

    @ResponseBody
    @PostMapping(ApiPaths.CHAT_ROOM_SEARCH_PUBLIC)
    public ResponseEntity<ResponseMessage> searchPublicRooms(
            String roomName,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ChatRoomInfo> publicRoomsPage = roomService.getPublicRoomsPage(roomName, pageable);

        return ResponseEntity.ok(new ResponseMessage("public rooms", publicRoomsPage));
    }

    @ResponseBody
    @PostMapping(ApiPaths.CHAT_ROOM_CREATE)
    public ResponseEntity<ResponseMessage> createRoom(
            @RequestBody CreateRoomRequestDto createRoomRequest
    ) {

        if (!rateLimiter.tryAcquire()) {
            Errors tooManyRequest = Errors.TOO_MANY_REQUEST;
            return ResponseMessage.errorResponseEntity(
                            tooManyRequest
                    );
        }

        roomService.createRoom(createRoomRequest.getRoomName(), createRoomRequest.getIsPrivate());

        return ResponseEntity.ok(new ResponseMessage("create room", "success"));
    }

    @GetMapping(ApiPaths.CHAT_ROOM_JOIN)
    public String joinRoom(
            @PathVariable Long roomId,
            Model model
    ) {
        try {
            roomService.chatRoomJoin(roomId);
        } catch (Exception e) {
            log.error("채팅방 정보 조회 실패", e);
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        memberAuthService.saveTokenToRedis();

        ChatRoomDetailInfo roomDetailInfo = roomService.getRoomDetailInfo(roomId);

        model.addAttribute("roomDetailInfo", roomDetailInfo);

        return "chat-room";
    }

    @ResponseBody()
    @PostMapping(ApiPaths.CHAT_ROOM_PREVIOUS_MESSAGES)
    public ResponseEntity<ResponseMessage> getPreviousMessages(
            @RequestBody PreviousChatMessageRequestDto previousChatMessageRequestDto
            ) {
        return ResponseEntity.ok(new ResponseMessage("previous messages", roomService.getPreviousMessages(
                previousChatMessageRequestDto.getRoomId()
                , previousChatMessageRequestDto.getLastMessageAt()
        )));
    }

    @ResponseBody
    @PostMapping(ApiPaths.CHAT_ROOM_EXIT)
    public ResponseEntity<ResponseMessage> exitRoom(
            @PathVariable Long roomId
    ) {
        roomService.chatRoomOut(roomId);

        return ResponseEntity.ok(new ResponseMessage("exit room", "success"));
    }

    @ResponseBody
    @PostMapping(ApiPaths.CHAT_ROOM_INVITE)
    public ResponseEntity<ResponseMessage> inviteRoom(
            @PathVariable Long roomId,
            @RequestBody InviteMemberRequestDto inviteMemberRequestDto
    ) {
        try {
            roomService.chatRoomInvite(roomId, inviteMemberRequestDto.getMemberNickname());
            return ResponseEntity.ok(new ResponseMessage("success", null));
        } catch (Exception e) {
            log.error("invite room failed", e);
            return ResponseEntity.badRequest().body(new ResponseMessage("failed", null));
        }


    }
}
