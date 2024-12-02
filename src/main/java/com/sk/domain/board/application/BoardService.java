package com.sk.domain.board.application;

import com.sk.domain.auth.domain.Member;
import com.sk.domain.auth.exception.AccessDeniedException;
import com.sk.domain.auth.exception.UserNotFoundException;
import com.sk.domain.auth.repository.AuthRepository;
import com.sk.domain.board.api.dto.*;
import com.sk.domain.board.domain.Attachment;
import com.sk.domain.board.domain.Board;
import com.sk.domain.board.exception.AttachmentNotFoundException;
import com.sk.domain.board.exception.BoardNotFoundException;
import com.sk.domain.board.repository.AttachmentRepository;
import com.sk.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 게시글 작성
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

    // 게시글 수정
    @Transactional
    public void updateBoard(Long memberId, Long boardId, BoardUpdateRequest boardUpdateRequest) {

        // 게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        // 작성자 확인
        if (!board.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException();
        }

        // 게시글 수정
        board.update(
                boardUpdateRequest.title(),
                boardUpdateRequest.content(),
                boardUpdateRequest.attachments() != null && !boardUpdateRequest.attachments().isEmpty()
        );

        // 첨부파일 삭제
        if (boardUpdateRequest.deleteAttachmentIds() != null && !boardUpdateRequest.deleteAttachmentIds().isEmpty()) {
            List<Attachment> attachmentsToDelete = attachmentRepository.findAllById(boardUpdateRequest.deleteAttachmentIds());

            for (Attachment attachment : attachmentsToDelete) {
                // 첨부파일이 이미 삭제된 경우 예외 발생
                if (attachment.isDeleted()) {
                    throw new AttachmentNotFoundException();
                }

                // 첨부파일 삭제 처리
                if (!attachment.getBoard().getId().equals(boardId)) {
                    throw new AttachmentNotFoundException();
                }

                attachment.delete();
            }
        }

        // 새로운 첨부파일 추가
        if (boardUpdateRequest.attachments() != null && !boardUpdateRequest.attachments().isEmpty()) {
            List<Attachment> newAttachments = boardUpdateRequest.attachments().stream()
                    .map(file -> createAttachment(board, file))
                    .toList();
            attachmentRepository.saveAll(newAttachments);
        }
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long memberId, Long boardId) {
        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(BoardNotFoundException::new);

        if (!board.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException();
        }

        board.delete();
    }

    // 게시글 상세 조회
    @Transactional
    public BoardDetailResponse getBoard(Long boardId) {

        // 삭제되지 않은 게시글 조회
        Board board = boardRepository.findByIdAndIsDeletedFalse(boardId)
                .orElseThrow(BoardNotFoundException::new);

        // 조회수 증가
        board.increaseViews();

        // 첨부파일 조회
        List<AttachmentResponse> attachments = board.getAttachments().stream()
                .filter(attachment -> !attachment.isDeleted()) // 삭제되지 않은 첨부파일만 포함
                .map(attachment -> new AttachmentResponse(
                        attachment.getId(),
                        attachment.getFileName()
                ))
                .toList();

        return new BoardDetailResponse(
                board.getId(),
                board.getMember().getEmail(),
                board.getTitle(),
                board.getContent(),
                board.getViews(),
                board.getCreatedAt(),
                attachments
        );
    }

    // 게시글 목록 조회
    public Page<BoardListResponse> getBoards(String keyword, Pageable pageable) {
        Page<Board> boards;

        if (keyword != null && !keyword.isEmpty()) {
            boards = boardRepository.findByTitleContainingOrMemberEmailContainingAndIsDeletedFalse(keyword, keyword, pageable);
        } else {
            boards = boardRepository.findAllByIsDeletedFalse(pageable);
        }

        return boards.map(board -> new BoardListResponse(
                board.getId(),
                board.getMember().getEmail(),
                board.getTitle(),
                board.getViews(),
                !board.getAttachments().isEmpty(),
                board.getCreatedAt()
        ));
    }
    
    private Attachment createAttachment(Board board, MultipartFile file) {
        try {
            return Attachment.of(board, file.getOriginalFilename(), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 데이터를 처리하는 중 오류가 발생했습니다.", e);
        }
    }
}
