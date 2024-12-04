package com.sk.domain.board.domain;

import com.sk.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "board_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ATTACHMENT_BOARD")
    )
    private Board board; // 게시글

    @Column(name = "file_name", nullable = false)
    private String fileName; // 파일 이름

    @Lob
    @Column(name = "file_data", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] fileData; // 파일 데이터

    public static Attachment of(Board board, String fileName, byte[] fileData) {
        Attachment attachment = new Attachment();
        attachment.board = board;
        attachment.fileName = fileName;
        attachment.fileData = fileData;
        return attachment;
    }
}
