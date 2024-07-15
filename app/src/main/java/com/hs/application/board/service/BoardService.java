package com.hs.application.board.service;

import com.hs.application.board.dto.BoardPostDetailResponseDto;
import com.hs.application.board.dto.BoardPostResponseDto;
import com.hs.application.board.exception.BoardPostNotFoundException;
import com.hs.persistence.entity.board.BoardPost;
import com.hs.persistence.entity.file.File;
import com.hs.persistence.entity.file.FileReferrer;
import com.hs.persistence.repository.board.BoardPostRepository;
import com.hs.persistence.repository.file.FileRepository;
import com.hs.persistence.repository.memeber.MemberRepository;
import com.hs.setting.utils.board.BoardProperties;
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
    private final BoardPostRepository boardPostRepository;

    public Page<BoardPostResponseDto> getNoticePage(Pageable pageable) {
        return boardPostRepository.findAllByBoardName(boardProperties.getNoticeBoardName(), pageable)
                .map(BoardPostResponseDto::new);
    }

    public BoardPostDetailResponseDto getNoticeDetail(Long postId) {

        BoardPost boardPost = boardPostRepository.findById(postId).orElseThrow(() -> {
            log.error("cannot find board post. id: {}", postId);
            return new BoardPostNotFoundException("게시글을 찾을 수 없습니다.");
        });

        BoardPostDetailResponseDto boardPostDetailResponseDto = new BoardPostDetailResponseDto(
                boardPost.getId(),
                boardPost.getTitle(),
                boardPost.getTextContent(),
                "",
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

}
