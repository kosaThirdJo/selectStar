package com.threestar.selectstar.dto.meeting.request;

import lombok.Getter;

@Getter
public class UpdateDeletedStatusRequest {
    private long meetingId;
    private int deleted;
}
