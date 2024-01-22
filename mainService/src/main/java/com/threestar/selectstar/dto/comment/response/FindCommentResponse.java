package com.threestar.selectstar.dto.comment.response;

import com.threestar.selectstar.entity.Comment;
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
    private Long commentId;
    private Long meetingId;
    private long userId;
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
