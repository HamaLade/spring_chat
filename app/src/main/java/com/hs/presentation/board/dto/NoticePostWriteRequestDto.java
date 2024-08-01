package com.hs.presentation.board.dto;

import lombok.Getter;

@Getter
public class NoticePostWriteRequestDto {

    private String title;
    private String textContent;

    public NoticePostWriteRequestDto() {
    }

    public NoticePostWriteRequestDto(String title, String textContent) {
        this.title = title;
        this.textContent = textContent;
    }
}
