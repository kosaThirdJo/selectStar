package com.threestar.selectstar.dto.apply.response;

import com.threestar.selectstar.entity.Apply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;



@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindApplyByUserIdResponse {
    private long userId;
    private Long meetingId;
    private String reason;
    private String emailAddress;
    private String snsAddress;
    private Date applicationDate;
    public static FindApplyByUserIdResponse fromEntity(Apply apply){
        return FindApplyByUserIdResponse.builder()
                .userId(apply.getApplyID().getUser().getUserId())
                .meetingId(apply.getApplyID().getMeeting().getMeetingId())
                .reason(apply.getReason())
                .emailAddress(apply.getEmailAddress())
                .snsAddress(apply.getSnsAddress())
                .applicationDate(apply.getApplicationDate())
                .build();
    }
}
