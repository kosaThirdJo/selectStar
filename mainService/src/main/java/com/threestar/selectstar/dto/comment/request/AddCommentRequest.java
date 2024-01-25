package com.threestar.selectstar.dto.comment.request;


import com.threestar.selectstar.entity.Comment;
import com.threestar.selectstar.entity.Meeting;
import com.threestar.selectstar.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCommentRequest {
    @NotBlank
    @Setter
    private int userId;
    @NotBlank
    @Setter
    private Long meetingId;
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
