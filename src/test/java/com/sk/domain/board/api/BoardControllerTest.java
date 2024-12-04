package com.sk.domain.board.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.domain.auth.api.dto.request.SignInRequest;
import com.sk.domain.auth.domain.Member;
import com.sk.domain.auth.repository.MemberRepository;
import com.sk.domain.board.domain.Attachment;
import com.sk.domain.board.repository.AttachmentRepository;
import com.sk.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository authRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        attachmentRepository.deleteAll();
        boardRepository.deleteAll();
        authRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 작성 성공 - 첨부파일 포함")
    void createBoardWithAttachments() throws Exception {
        // given
        MockMultipartFile attachment1 = new MockMultipartFile(
                "attachments", "test1.txt", "text/plain", "Test File 1".getBytes());
        MockMultipartFile attachment2 = new MockMultipartFile(
                "attachments", "test2.txt", "text/plain", "Test File 2".getBytes());
        String title = "게시글 제목";
        String content = "게시글 내용";

        // when
        mockMvc.perform(multipart("/api/board")
                        .file(attachment1)
                        .file(attachment2)
                        .param("title", title)
                        .param("content", content)
                        .header("Authorization", getAccessToken()))
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        List<Attachment> attachments = attachmentRepository.findAll();
        assertEquals(2, attachments.size());
        assertEquals("test1.txt", attachments.get(0).getFileName());
        assertEquals("test2.txt", attachments.get(1).getFileName());
    }

    @Test
    @DisplayName("게시글 작성 성공 - 첨부파일 없이")
    void createBoardWithoutAttachments() throws Exception {
        // given
        String title = "게시글 제목";
        String content = "게시글 내용";

        // when
        mockMvc.perform(multipart("/api/board")
                        .param("title", title)
                        .param("content", content)
                        .header("Authorization", getAccessToken()))
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        List<Attachment> attachments = attachmentRepository.findAll();
        assertTrue(attachments.isEmpty());
    }

    @Test
    @DisplayName("게시글 작성 실패 - 첨부파일 6개 초과")
    void createBoardWithTooManyAttachments() throws Exception {
        // given: 첨부파일 6개 생성
        MockMultipartFile attachment1 = new MockMultipartFile("attachments", "test1.txt", "text/plain", "Test File 1".getBytes());
        MockMultipartFile attachment2 = new MockMultipartFile("attachments", "test2.txt", "text/plain", "Test File 2".getBytes());
        MockMultipartFile attachment3 = new MockMultipartFile("attachments", "test3.txt", "text/plain", "Test File 3".getBytes());
        MockMultipartFile attachment4 = new MockMultipartFile("attachments", "test4.txt", "text/plain", "Test File 4".getBytes());
        MockMultipartFile attachment5 = new MockMultipartFile("attachments", "test5.txt", "text/plain", "Test File 5".getBytes());
        MockMultipartFile attachment6 = new MockMultipartFile("attachments", "test6.txt", "text/plain", "Test File 6".getBytes());

        String title = "게시글 제목";
        String content = "게시글 내용";

        // when & then
        mockMvc.perform(multipart("/api/board")
                        .file(attachment1)
                        .file(attachment2)
                        .file(attachment3)
                        .file(attachment4)
                        .file(attachment5)
                        .file(attachment6) // 6개 첨부
                        .param("title", title)
                        .param("content", content)
                        .header("Authorization", getAccessToken()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDetail.message").value("첨부파일은 최대 5개까지 업로드할 수 있습니다."))
                .andDo(print());
    }

    // AccessToken을 발급받는 메서드
    private String getAccessToken() throws Exception {
        // given: 테스트용 사용자 등록
        Member member = authRepository.save(Member.of(
                "yezy@gmail.com",
                "이예지",
                passwordEncoder.encode("password123")
        ));

        // 로그인 요청
        String response = mockMvc.perform(post("/api/auth/sign-in")
                        .content(objectMapper.writeValueAsString(new SignInRequest("yezy@gmail.com", "password123")))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // JSON 응답에서 AccessToken 추출
        return "Bearer " + objectMapper.readTree(response)
                .path("data")
                .path("accessToken")
                .asText();
    }
}