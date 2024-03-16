package com.threestar.boardService.dto.user.request;

import lombok.Getter;

@Getter
public class UpdateUserStatusRequest {
    private Integer userId;
    private int userStatus;
}
