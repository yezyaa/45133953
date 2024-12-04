package com.sk.domain.board.exception;

import com.sk.global.exception.BusinessException;

public class AttachmentNotFoundException extends BusinessException {
    public AttachmentNotFoundException() {
        super(BoardErrorCode.ATTACHMENT_NOT_FOUND);
    }
}
