package com.hs.persistence;

import com.hs.persistence.entity.board.BoardPost;
import com.hs.persistence.repository.board.BoardPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Slf4j
@DataJpaTest
@Sql({"/sql/test/schema.sql", "/sql/test/data.sql"})
@ActiveProfiles("test")
@DisplayName("BoardPostRepository 테스트")
public class BoardPostRepositoryTest {

    @Autowired
    BoardPostRepository boardPostRepository;

    @Test
    @DisplayName("게시글 조회")
    void findByBoardNameTest() {

        String boardName = "notice";
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        Page<BoardPost> noticePostPage = boardPostRepository.findAllByBoardName(boardName, pageable);

        Assertions.assertTrue(!noticePostPage.isEmpty());
    }

}
