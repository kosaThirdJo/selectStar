package com.threestar.selectstar.dto.comment.request;

import lombok.Getter;

@Getter
public class UpdateDeletedStatusRequest {
    private long commentId;
    private int deleted;
}
