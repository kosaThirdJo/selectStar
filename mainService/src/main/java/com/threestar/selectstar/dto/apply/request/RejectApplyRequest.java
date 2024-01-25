package com.threestar.selectstar.dto.apply.request;

import lombok.*;


@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RejectApplyRequest {
    private Long meetingId;
    @Setter
    private int userId;
    private String reason;


}
