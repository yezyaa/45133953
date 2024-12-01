package com.sk.domain.board.application;

import com.sk.domain.auth.domain.Member;
import com.sk.domain.auth.exception.UserNotFoundException;
import com.sk.domain.auth.repository.AuthRepository;
import com.sk.domain.board.api.dto.BoardCreateRequest;
import com.sk.domain.board.domain.Attachment;
import com.sk.domain.board.domain.Board;
import com.sk.domain.board.repository.AttachmentRepository;
import com.sk.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final AuthRepository authRepository;
    private final BoardRepository boardRepository;
    private final AttachmentRepository attachmentRepository;

    @Transactional
    public void createBoard(Long memberId, BoardCreateRequest boardCreateRequest) {

        // 로그인 사용자 조회
        Member member = authRepository.findById(memberId)
                .orElseThrow(UserNotFoundException::new);

        // 게시글 생성 및 저장
        Board board = Board.of(
                member,
                boardCreateRequest.title(),
                boardCreateRequest.content(),
                boardCreateRequest.attachments() != null && !boardCreateRequest.attachments().isEmpty());
        boardRepository.save(board);

        // 첨부파일 처리 및 저장
        if (boardCreateRequest.attachments() != null && !boardCreateRequest.attachments().isEmpty()) {
            List<Attachment> attachments = boardCreateRequest.attachments().stream()
                    .map(file -> createAttachment(board, file))
                    .toList();
            attachmentRepository.saveAll(attachments);
        }
    }

    private Attachment createAttachment(Board board, MultipartFile file) {
        try {
            return Attachment.of(board, file.getOriginalFilename(), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 데이터를 처리하는 중 오류가 발생했습니다.", e);
        }
    }
}
