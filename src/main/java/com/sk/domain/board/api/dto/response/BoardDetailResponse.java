package com.sk.domain.board.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record BoardDetailResponse(
        Long id,
        String email,
        String title,
        String content,
        int views,
        LocalDateTime createdAt,
        List<AttachmentResponse> attachments
) {
}