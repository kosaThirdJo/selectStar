package com.threestar.selectstar.dto.meeting.response;

import com.threestar.selectstar.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class GetMeetingsResponse {
    private long meetingId;
    private String userName;
    private String title;
    private int views;
    private Date creationDate;
    private int status;
    private int deleted;

    public static GetMeetingsResponse fromEntity(Meeting meeting){
        return GetMeetingsResponse.builder()
                .meetingId(meeting.getMeetingId())
                .userName(meeting.getUser().getName())
                .title(meeting.getTitle())
                .views(meeting.getViews())
                .creationDate(meeting.getCreationDate())
                .status(meeting.getStatus())
                .deleted(meeting.getDeleted())
                .build();
    }
}
