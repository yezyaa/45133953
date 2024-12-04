package com.sk.domain.board.exception;

import com.sk.global.exception.BusinessException;

public class BoardNotFoundException extends BusinessException {
    public BoardNotFoundException() {
        super(BoardErrorCode.BOARD_NOT_FOUND);
    }
}
