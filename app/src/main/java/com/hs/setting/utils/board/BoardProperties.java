package com.hs.setting.utils.board;

import lombok.Getter;

@Getter
public class BoardProperties {

    public final String noticeBoardName;
    public static final String NOTICE_BOARD_NAME = "notice";

    public BoardProperties(
            String noticeBoardName
    ) {
        this.noticeBoardName = noticeBoardName;
    }
}
