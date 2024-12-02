package com.sk.domain.board.repository;

import com.sk.domain.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByIdAndIsDeletedFalse(Long boardId);
    Page<Board> findAllByIsDeletedFalse(Pageable pageable);

}
