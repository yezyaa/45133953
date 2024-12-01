package com.sk.domain.board.api;

import com.sk.domain.board.api.dto.BoardCreateRequest;
import com.sk.domain.board.application.BoardService;
import com.sk.global.dto.ApiSuccessResponse;
import com.sk.global.security.CustomUserDetails;
import com.sk.global.security.annotations.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
