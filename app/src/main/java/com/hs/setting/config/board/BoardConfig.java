package com.hs.setting.config.board;

import com.hs.setting.utils.board.BoardProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoardConfig {

    private final String noticeBoardName;

    public BoardConfig(
            @Value("${board.notice.board.name}") String noticeBoardName
    ) {
        this.noticeBoardName = noticeBoardName;
    }

    @Bean
    public BoardProperties boardProperties() {
        return new BoardProperties(
                noticeBoardName
        );
    }

}
