package com.sk.domain.board.domain;

import com.sk.domain.auth.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    @Column(nullable = false, length = 100)
    private String title; // 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 내용

    @Column(nullable = false)
    private int views = 0; // 조회수

    @Column(name = "has_attachments", nullable = false)
    private boolean hasAttachments = false; // 첨부파일 여부

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 삭제 여부

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();

    public static Board of(Member member, String title, String content, boolean hasAttachments) {
        Board board = new Board();
        board.member = member;
        board.title = title;
        board.content = content;
        board.hasAttachments = hasAttachments;
        return board;
    }

    public void update(String title, String content, boolean hasAttachments) {
        this.title = title;
        this.content = content;
        this.hasAttachments = hasAttachments;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void increaseViews() {
        this.views++;
    }
}
