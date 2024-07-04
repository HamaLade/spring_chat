package com.hs.application;

import com.hs.application.board.dto.BoardPostDetailResponseDto;
import com.hs.application.board.dto.BoardPostResponseDto;
import com.hs.application.board.service.BoardService;
import com.hs.persistence.entity.board.BoardPost;
import com.hs.persistence.repository.board.BoardPostRepository;
import com.hs.persistence.repository.board.BoardRepository;
import com.hs.utils.board.BoardProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("회원 인증 서비스 테스트")
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardPostRepository boardPostRepository;

    @Mock
    private BoardProperties boardProperties;

    @InjectMocks
    private BoardService boardService;

    @Test
    @DisplayName("공지 페이지 조회")
    void noticePageTest() {

        String noticeBoardName = "notice";
        long noticeBoardId = 1L;
        List<BoardPost> content = Arrays.asList(
                BoardPost.createTestBoardPost(1L, noticeBoardId, null, "Title 1", "Content 1", false),
                BoardPost.createTestBoardPost(2L, noticeBoardId, null, "Title 1", "Content 2", false)
        );
        Page<BoardPost> page = new PageImpl<>(content);
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        Mockito.when(boardProperties.getNoticeBoardName()).thenReturn(noticeBoardName);
        Mockito.when(boardPostRepository.findAllByBoardName(noticeBoardName, pageable)).thenReturn(page);

        Page<BoardPostResponseDto> noticePage = boardService.getNoticePage(pageable);

        Assertions.assertEquals(content.size(), noticePage.getContent().size());
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void noticeDetailTest() {

        long postId = 1L;
        BoardPost boardPost = BoardPost.createTestBoardPost(postId, 1L, null, "Title", "Content", false);

        Mockito.when(boardPostRepository.findById(postId)).thenReturn(Optional.of(boardPost));

        BoardPostDetailResponseDto noticeDetail = boardService.getNoticeDetail(postId);

        Assertions.assertEquals(boardPost.getId(), noticeDetail.getId());

    }

}
