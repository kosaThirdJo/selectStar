package com.threestar.selectstar.dto.meeting.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompleteRequest {
    private int userId;
    private int meetingId;
}
