package com.hs.setting.utils.board;

import lombok.Getter;

@Getter
public class BoardProperties {

    public final String noticeBoardName;

    public BoardProperties(
            String noticeBoardName
    ) {
        this.noticeBoardName = noticeBoardName;
    }
}