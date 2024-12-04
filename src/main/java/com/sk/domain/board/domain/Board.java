package com.sk.domain.board.domain;

import com.sk.domain.auth.domain.Member;
import com.sk.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Board extends BaseEntity {

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

    public void increaseViews() {
        this.views++;
    }
}
