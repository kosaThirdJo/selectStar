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
public class UpdateCommentRequest {
    @Setter
    @NotBlank
    private String content;
}
