package com.sk.domain.board.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record BoardUpdateRequest(
        @NotBlank(message = "제목을 입력해 주세요.")
        String title,

        @NotBlank(message = "내용을 입력해 주세요.")
        String content,

        // 삭제할 첨부파일
        List<Long> deleteAttachmentIds,

        @Size(max = 5, message = "첨부파일은 최대 5개까지 업로드할 수 있습니다.")
        List<MultipartFile> attachments
) {}
