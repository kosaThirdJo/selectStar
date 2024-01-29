package com.threestar.boardService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    USER_NOT_FOUND(404, "사용자가 존재하지 않습니다."),
    BOARD_NOT_FOUND(404, "게시글 존재하지 않습니다."),
    BOARD_EXIST(404, "게시글이 이미 존재합니다."),
    DUPLICATE_RESOURCE(409, "데이터가 이미 존재합니다.");

    private final int status;

    private final String message;

}
