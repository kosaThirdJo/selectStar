package com.threestar.selectstar.dto.meeting.request;

import lombok.Getter;

@Getter
public class UpdateDeletedRequest {
    private long meetingId;
    private int deleted;
}
