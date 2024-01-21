package com.threestar.selectstar.dto.apply.response;


import com.threestar.selectstar.entity.Apply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCheckResponse {
    String reason;
    String rejectReason;
    Integer applyStatus;
    public static ApplyCheckResponse fromEntity(Apply apply){
        return ApplyCheckResponse.builder()
                .reason(apply.getReason())
                .rejectReason(apply.getRejectReason())
                .applyStatus(apply.getApplyStatus())
                .build();
    }

}
