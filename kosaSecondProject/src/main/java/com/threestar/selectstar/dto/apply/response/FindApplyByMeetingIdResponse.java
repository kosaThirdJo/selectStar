package com.threestar.selectstar.dto.apply.response;

import com.threestar.selectstar.domain.entity.Apply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindApplyByMeetingIdResponse {
    private int userId;
    private int meetingId;
    private String emailAddress;
    private String snsAddress;
    private Date applicationDate;
    public static FindApplyByMeetingIdResponse fromEntity(Apply apply){
        return FindApplyByMeetingIdResponse.builder()
                .userId(apply.getApplyID().getUser().getUserId())
                .meetingId(apply.getApplyID().getMeeting().getMeetingId())
                .emailAddress(apply.getEmailAddress())
                .snsAddress(apply.getSnsAddress())
                .applicationDate(apply.getApplicationDate())
                .build();
    }
}
