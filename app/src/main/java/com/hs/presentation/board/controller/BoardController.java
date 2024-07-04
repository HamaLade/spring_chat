package com.hs.presentation.board.controller;

import com.hs.application.board.dto.BoardPostDetailResponseDto;
import com.hs.application.board.dto.BoardPostResponseDto;
import com.hs.application.board.service.BoardService;
import com.hs.presentation.ApiPaths;
import com.hs.presentation.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

        return ResponseEntity.ok(new ResponseMessage("notice board", noticePage));
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
        BoardPostDetailResponseDto noticeDetail = boardService.getNoticeDetail(postId);
        model.addAttribute("post", noticeDetail);
        return "post-detail";
    }

}
