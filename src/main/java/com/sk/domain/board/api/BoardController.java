package com.sk.domain.board.api;

import com.sk.domain.board.api.dto.BoardCreateRequest;
import com.sk.domain.board.api.dto.BoardDetailResponse;
import com.sk.domain.board.api.dto.BoardListResponse;
import com.sk.domain.board.api.dto.BoardUpdateRequest;
import com.sk.domain.board.application.BoardService;
import com.sk.global.dto.ApiSuccessResponse;
import com.sk.global.security.CustomUserDetails;
import com.sk.global.security.annotations.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<Void>> createBoard(
            @CurrentUser CustomUserDetails userDetails,
            @Valid @ModelAttribute BoardCreateRequest boardCreateRequest,
            HttpServletRequest servletRequest
    ) {
        boardService.createBoard(userDetails.getMemberId(), boardCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiSuccessResponse.of(
                        HttpStatus.CREATED,
                        servletRequest.getServletPath(),
                        null
                ));
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<ApiSuccessResponse<Void>> updateBoard(
            @CurrentUser CustomUserDetails userDetails,
            @PathVariable Long boardId,
            @Valid @ModelAttribute BoardUpdateRequest boardUpdateRequest,
            HttpServletRequest servletRequest
    ) {
        boardService.updateBoard(userDetails.getMemberId(), boardId, boardUpdateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResponse.of(
                        HttpStatus.OK,
                        servletRequest.getServletPath(),
                        null
                ));
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteBoard(
            @CurrentUser CustomUserDetails userDetails,
            @PathVariable Long boardId,
            HttpServletRequest servletRequest
    ) {
        boardService.deleteBoard(userDetails.getMemberId(), boardId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiSuccessResponse.of(
                        HttpStatus.NO_CONTENT,
                        servletRequest.getServletPath(),
                        null
                ));
    }

    // 게시글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiSuccessResponse<BoardDetailResponse>> getBoard(
            @PathVariable Long boardId,
            HttpServletRequest servletRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResponse.of(
                        HttpStatus.OK,
                        servletRequest.getServletPath(),
                        boardService.getBoard(boardId)
                ));
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<Page<BoardListResponse>>> getBoards(
            @RequestParam(required = false) String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest servletRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResponse.of(
                        HttpStatus.OK,
                        servletRequest.getServletPath(),
                        boardService.getBoards(keyword, pageable)
                ));
    }
}
