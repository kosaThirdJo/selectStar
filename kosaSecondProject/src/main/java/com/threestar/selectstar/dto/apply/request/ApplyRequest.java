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
public class ApplyRequest {
    @NotBlank
    private Integer userId;
    @NotBlank
    private Integer meetingId;
    @NotBlank
    private String emailAddress;
    private String snsAddress;
    @NotBlank
    private String reason;
    private Date applicationDate;

    public static Apply toEntity(ApplyRequest request, ApplyID applyID){
        return Apply.builder()
                .applyID(applyID)
                .emailAddress(request.emailAddress)
                .snsAddress(request.snsAddress)
                .reason(request.reason)
                .applicationDate(request.applicationDate)
                .build();
    }
}
