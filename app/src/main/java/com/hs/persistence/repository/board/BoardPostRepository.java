package com.hs.persistence.repository.board;

import com.hs.persistence.entity.board.BoardPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    // 공지사항 게시글 조회
    @Query("select bp from BoardPost bp where bp.boardId = (select b.id from Board b where b.boardName = :boardName)")
    Page<BoardPost> findAllByBoardName(String boardName, Pageable pageable);


}
