package com.threestar.selectstar.dto.comment.response;

import com.threestar.selectstar.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;
@Getter
@Builder
@ToString
@AllArgsConstructor
public class GetCommentsResponse {
    private long commentId;
    private long meetingId;
    private String userName;
    private String content;
    private Date creationDate;
    private int deleted;

    public static GetCommentsResponse fromEntity(Comment comment){
        return GetCommentsResponse.builder()
                .commentId(comment.getCommentId())
                .meetingId(comment.getMeeting().getMeetingId())
                .userName(comment.getUser().getName())
                .content(comment.getContent())
                .creationDate(comment.getCreationDate())
                .deleted(comment.getDeleted())
                .build();
    }
}
