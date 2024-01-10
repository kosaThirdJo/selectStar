package com.threestar.selectstar.dto.comment.response;

import com.threestar.selectstar.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;



@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCommentResponse {
    private int commentId;
    private int meetingId;
    private int userId;
    private String userNickName;
    private String content;
    private Date creationDate;

    public static FindCommentResponse fromEntity(Comment comment) {
        return FindCommentResponse.builder()
                .commentId(comment.getCommentId())
                .meetingId(comment.getMeeting().getMeetingId())
                .userId(comment.getUser().getUserId())
                .userNickName(comment.getUser().getNickname())
                .content(comment.getContent())
                .creationDate(comment.getCreationDate())
                .build();
    }
}
