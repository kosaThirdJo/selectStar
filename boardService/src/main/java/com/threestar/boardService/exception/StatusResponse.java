package com.threestar.boardService.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class StatusResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String code;
    private final String message;

    public static ResponseEntity<StatusResponse> toResponseEntity(ExceptionCode exceptionCode) {
        return ResponseEntity
                .status(exceptionCode.getStatus())
                .body(StatusResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(exceptionCode.getStatus())
                        .code(exceptionCode.name())
                        .message(exceptionCode.getMessage())
                        .build()
                );
    }
}