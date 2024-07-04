package com.hs.persistence.repository.board;


import com.hs.persistence.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByIsActivated(boolean isActivated);

}
