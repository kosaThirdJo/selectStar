package com.threestar.selectstar.dto.comment.request;


import com.threestar.selectstar.domain.entity.Comment;
import com.threestar.selectstar.domain.entity.Meeting;
import com.threestar.selectstar.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCommentRequest {
    @NotBlank
    private int userId;
    @NotBlank
    private int meetingId;
    @NotBlank
    private String content;

    public static Comment toEntity(AddCommentRequest addCommentRequest, User user, Meeting meeting){
        return Comment.builder()
                .user(user)
                .meeting(meeting)
                .content(addCommentRequest.getContent())
                .build();
    }



}
