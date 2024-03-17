package com.threestar.selectstar.dto.apply.request;

import com.threestar.selectstar.entity.Apply;
import com.threestar.selectstar.entity.ApplyID;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyRequest {
    @NotBlank
    @Setter
    private Integer userId;
    @NotBlank
    private Long meetingId;
    @NotBlank
    private String emailAddress;
    private String snsAddress;
    @NotBlank
    private String reason;
    private Date applicationDate;
    @Setter
    private Integer applyStatus;

    public static Apply toEntity(ApplyRequest request, ApplyID applyID){
        return Apply.builder()
                .applyID(applyID)
                .emailAddress(request.emailAddress)
                .snsAddress(request.snsAddress)
                .reason(request.reason)
                .applicationDate(request.applicationDate)
                .applyStatus(request.applyStatus)
                .build();
    }
}
