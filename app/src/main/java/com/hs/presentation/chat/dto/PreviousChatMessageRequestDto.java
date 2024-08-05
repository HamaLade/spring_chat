package com.hs.presentation.chat.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PreviousChatMessageRequestDto {
    private Long roomId;
    private LocalDateTime lastMessageAt;
}
