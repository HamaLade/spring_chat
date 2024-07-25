package com.hs.application.board.service;

import com.hs.application.board.dto.BoardPostDetailResponseDto;
import com.hs.application.board.dto.BoardPostResponseDto;
import com.hs.application.board.exception.BoardPostNotFoundException;
import com.hs.persistence.entity.board.Board;
import com.hs.persistence.entity.board.BoardPost;
import com.hs.persistence.entity.file.File;
import com.hs.persistence.entity.file.FileReferrer;
import com.hs.persistence.repository.board.BoardPostRepository;
import com.hs.persistence.repository.board.BoardRepository;
import com.hs.persistence.repository.file.FileRepository;
import com.hs.persistence.repository.memeber.MemberRepository;
import com.hs.setting.utils.board.BoardProperties;
import com.hs.setting.utils.markdown.MarkDownUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;
    private final BoardProperties boardProperties;
    private final BoardRepository boardRepository;
    private final BoardPostRepository boardPostRepository;

    /**
     * 공지사항 게시글 페이지 조회
     *
     * @param pageable 요청 페이지 정보
     * @return Page<BoardPostResponseDto> 공지사항 게시글 페이지
     */
    public Page<BoardPostResponseDto> getNoticePage(Pageable pageable) {
        return boardPostRepository.findAllByBoardName(boardProperties.getNoticeBoardName(), pageable)
                .map(BoardPostResponseDto::new);
    }

    /**
     * 공지사항 게시글 상세 조회
     *
     * @param postId 게시글 ID
     * @return BoardPostDetailResponseDto 게시글 상세 정보
     */
    public BoardPostDetailResponseDto getNoticeDetail(Long postId) {

        BoardPost boardPost = boardPostRepository.findById(postId).orElseThrow(BoardPostNotFoundException::new);

        BoardPostDetailResponseDto boardPostDetailResponseDto = new BoardPostDetailResponseDto(
                boardPost.getId(),
                boardPost.getTitle(),
                boardPost.getTextContent(),
                "", // 글 쓴이의 ID는 nullable (어드민 공지)
                boardPost.getCreateDate(),
                boardPost.getUpdateDate(),
                null
        );

        if (boardPost.getWriterId() != null) {
            boardPostDetailResponseDto.setWriterNickname(
                    Objects.requireNonNull(memberRepository.findById(boardPost.getWriterId())
                            .orElse(null)
                    ).getNickname()
            );
        }

        if (boardPost.isHasFile()) {
            List<File> byReferrerIdAndFileReferrer = fileRepository.findByReferrerIdAndFileReferrer(boardPost.getId(), FileReferrer.BOARD);
            boardPostDetailResponseDto.setFiles(byReferrerIdAndFileReferrer);
        }

        return boardPostDetailResponseDto;
    }

    /**
     * 공지사항 게시글 작성
     *
     * @param title 게시글 제목
     * @param content 게시글 내용
     */
    public void writeNoticePost(String title,
                                String content) {

        Board noticeBoard = boardRepository.findByBoardName(boardProperties.getNoticeBoardName())
                .orElseThrow(() -> new IllegalArgumentException("공지사항 게시판이 존재하지 않습니다."));

        BoardPost boardPost = new BoardPost(
                noticeBoard.getId(),
                title,
                content,
                false
        );

        boardPostRepository.save(boardPost);
    }

    /**
     * 공지사항 게시글 수정
     *
     * @param postId 게시글 ID
     * @param title 게시글 제목
     * @param content 게시글 내용
     */
    public void editNoticePost(Long postId,
                               String title,
                               String content) {

        BoardPost boardPost = boardPostRepository.findById(postId).orElseThrow(BoardPostNotFoundException::new);

        boardPost.updateTitle(title);
        boardPost.updateTextContent(content);

        boardPostRepository.save(boardPost);

    }

}
