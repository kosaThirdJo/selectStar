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
public class FindApplyByMeetingIdValidResponse {
    private String userName;
    private int userId;
    private int meetingId;
    private String emailAddress;
    private String snsAddress;
    private Date applicationDate;
    private String reason;
    public static FindApplyByMeetingIdValidResponse fromEntity(Apply apply){
        return FindApplyByMeetingIdValidResponse.builder()
                .userName(apply.getApplyID().getUser().getNickname())
                .userId(apply.getApplyID().getUser().getUserId())
                .meetingId(apply.getApplyID().getMeeting().getMeetingId())
                .emailAddress(apply.getEmailAddress())
                .snsAddress(apply.getSnsAddress())
                .applicationDate(apply.getApplicationDate())
                .reason(apply.getReason())
                .build();
    }
}
