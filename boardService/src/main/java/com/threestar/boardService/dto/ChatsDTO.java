package com.threestar.boardService.dto;

import com.threestar.boardService.entity.Chats;
import com.threestar.boardService.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatsDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDTO {
        @NotNull(message = "제목은 필수입니다.")
        private String title;

        @NotNull(message = "내용은 필수입니다.")
        private String content;

        private int deleted; // 0:삭제X 1:삭제

        private java.sql.Date creationDate;
        private User user;

        //TODO 작성자 추가
        public Chats toEntity() {
            return Chats.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .deleted(deleted)
                    .creationDate(creationDate)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDTO {
        @NotNull(message = "제목은 필수입니다.")
        private String title;

        @NotNull(message = "내용은 필수입니다.")
        private String content;

        private User user;

        private java.sql.Date creationDate;
    }
}
