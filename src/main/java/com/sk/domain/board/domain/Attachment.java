package com.sk.domain.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board; // 게시글

    @Column(name = "file_name", nullable = false)
    private String fileName; // 파일 이름

    @Column(name = "file_url", nullable = false)
    private String fileUrl; // 파일 경로

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 삭제 여부

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static Attachment of(Board board, String fileName, String fileUrl) {
        Attachment attachment = new Attachment();
        attachment.board = board;
        attachment.fileName = fileName;
        attachment.fileUrl = fileUrl;
        return attachment;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
