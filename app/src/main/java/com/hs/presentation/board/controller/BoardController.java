package com.hs.presentation.board.controller;

import com.hs.application.board.dto.BoardPostDetailResponseDto;
import com.hs.application.board.dto.BoardPostResponseDto;
import com.hs.application.board.service.BoardService;
import com.hs.presentation.ApiPaths;
import com.hs.presentation.ResponseMessage;
import com.hs.presentation.board.dto.NoticePostWriteRequestDto;
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
 * 게시판 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @ResponseBody
    @PostMapping(ApiPaths.GET_NOTICE_BOARD)
    public ResponseEntity<ResponseMessage> getNoticeBoard(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<BoardPostResponseDto> noticePage = boardService.getNoticePage(pageable);

        return ResponseEntity.ok(new ResponseMessage("ok", noticePage));
    }

    @GetMapping(ApiPaths.GET_NOTICE_BOARD_PAGE)
    public String getNoticeBoardPage(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<BoardPostResponseDto> noticePage = boardService.getNoticePage(pageable);
        model.addAttribute("noticePage", noticePage);
        return "notice-page";
    }

    @GetMapping(ApiPaths.GET_BOARD_POST_DETAIL)
    public String getBoardPostDetail(@PathVariable Long postId, Model model) {
        try {
            BoardPostDetailResponseDto noticeDetail = boardService.getNoticeDetail(postId);
            noticeDetail.textContentMarkDownToHtml();
            model.addAttribute("post", noticeDetail);
        } catch (Exception e) {
            log.error("게시물 읽기 조회 실패", e);
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "post-detail";
    }

    @GetMapping(ApiPaths.WRITE_NOTICE_POST)
    public String writeNoticePost() {
        return "write-notice";
    }

    @ResponseBody
    @PostMapping(ApiPaths.WRITE_NOTICE_POST)
    public ResponseEntity<ResponseMessage> writeNoticePost(
            @RequestBody NoticePostWriteRequestDto noticePostWriteRequestDto
    ) {
        boardService.writeNoticePost(noticePostWriteRequestDto.getTitle(), noticePostWriteRequestDto.getTextContent());
        return ResponseEntity.ok(new ResponseMessage("ok"));
    }

    @GetMapping(ApiPaths.EDIT_NOTICE_POST)
    public String editNoticePost(@PathVariable Long postId, Model model) {
        try {
            BoardPostDetailResponseDto noticeDetail = boardService.getNoticeDetail(postId);
            model.addAttribute("post", noticeDetail);
        } catch (Exception e) {
            log.error("게시물 수정 요청 조회 실패", e);
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "edit-notice";
    }

    @ResponseBody
    @PostMapping(ApiPaths.EDIT_NOTICE_POST)
    public ResponseEntity<ResponseMessage> editNoticePost(
            @PathVariable Long postId,
            @RequestBody NoticePostWriteRequestDto noticePostWriteRequestDto
    ) {
        boardService.editNoticePost(postId, noticePostWriteRequestDto.getTitle(), noticePostWriteRequestDto.getTextContent());
        return ResponseEntity.ok(new ResponseMessage("ok"));
    }

}
