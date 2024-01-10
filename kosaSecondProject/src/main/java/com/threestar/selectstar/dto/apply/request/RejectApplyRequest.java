package com.threestar.selectstar.dto.apply.request;

import com.threestar.selectstar.domain.entity.Apply;
import com.threestar.selectstar.domain.entity.ApplyID;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;


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
