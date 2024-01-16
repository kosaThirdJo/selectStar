package com.threestar.selectstar.dto.apply.request;

import lombok.*;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RejectApplyRequest {
    private int meetingId;
    private int userId;
    private String reason;


}
