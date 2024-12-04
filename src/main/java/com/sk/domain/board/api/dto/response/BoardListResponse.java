package com.sk.domain.board.api.dto.response;

import java.time.LocalDateTime;

public record BoardListResponse(
        Long id,
        String email,
        String title,
        int views,
        boolean hasAttachment,
        LocalDateTime createdAt
) {}
