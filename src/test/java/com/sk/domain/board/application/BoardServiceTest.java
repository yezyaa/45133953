package com.sk.domain.board.application;

import com.sk.domain.auth.domain.Member;
import com.sk.domain.auth.repository.AuthRepository;
import com.sk.domain.board.api.dto.request.BoardCreateRequest;
import com.sk.domain.board.domain.Attachment;
import com.sk.domain.board.domain.Board;
import com.sk.domain.board.repository.AttachmentRepository;
import com.sk.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @AfterEach
    void clean() {
        attachmentRepository.deleteAll();
        boardRepository.deleteAll();
        authRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 작성 성공 - 첨부파일 포함")
    void createBoardWithAttachments() {
        // given
        Member member = authRepository.save(Member.of("yezy@gmail.com", "이예지", "password123"));
        MockMultipartFile file1 = new MockMultipartFile("attachments", "test1.txt", "text/plain", "Test File 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("attachments", "test2.txt", "text/plain", "Test File 2".getBytes());

        BoardCreateRequest request = new BoardCreateRequest("제목", "내용", List.of(file1, file2));

        // when
        boardService.createBoard(member.getId(), request);

        // then
        List<Attachment> attachments = attachmentRepository.findAll();
        assertEquals(2, attachments.size());
        assertEquals("test1.txt", attachments.get(0).getFileName());
        assertEquals("test2.txt", attachments.get(1).getFileName());

        Board board = boardRepository.findAll().get(0);
        assertEquals("제목", board.getTitle());
        assertEquals("내용", board.getContent());
    }

    @Test
    @DisplayName("게시글 작성 성공 - 첨부파일 없이")
    void createBoardWithoutAttachments() {
        // given
        Member member = authRepository.save(Member.of("yezy@gmail.com", "이예지", "password123"));
        BoardCreateRequest request = new BoardCreateRequest("제목", "내용", null);

        // when
        boardService.createBoard(member.getId(), request);

        // then
        List<Attachment> attachments = attachmentRepository.findAll();
        assertTrue(attachments.isEmpty());

        Board board = boardRepository.findAll().get(0);
        assertEquals("제목", board.getTitle());
        assertEquals("내용", board.getContent());
    }
}