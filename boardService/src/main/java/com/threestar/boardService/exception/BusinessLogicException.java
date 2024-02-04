package com.threestar.boardService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@AllArgsConstructor
public class BusinessLogicException extends RuntimeException {
    private final ExceptionCode exceptionCode;
}