package com.hs.application.board.dto;

import com.hs.persistence.entity.board.BoardPost;
import lombok.Getter;

@Getter
public class BoardPostResponseDto {

    private Long id;
    private String title;
    private String textContent;

    public BoardPostResponseDto(BoardPost boardPost) {
        this.id = boardPost.getId();
        this.title = boardPost.getTitle();
        this.textContent = boardPost.getTextContent();
    }

}
