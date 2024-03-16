package com.threestar.selectstar.dto.meeting.request;

import com.threestar.selectstar.entity.Meeting;
import com.threestar.selectstar.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.sql.Date;


@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUpdateMeetingRequest {
    private Long meetingId;
    @Setter
    private Integer userId;
    @NotBlank(message = "title must not be blank")
    private String title;
    private Integer category;
    private Integer status;
    private Date applicationDeadline;
    private Integer views;
    @NotNull(message = "recruitmentCount must not be null")
    private Integer recruitmentCount;
    private Integer applicationCount;
    @NotBlank(message = "location must not be blank")
    private String location;
    @NotBlank(message = "description must not be blank")
    private String description;
    private Date creationDate;
    private String interestLanguage;
    private String interestFramework;
    private String interestJob;
    public static Meeting toEntity(AddUpdateMeetingRequest addUpdateMeetingRequest, User user) {
        return Meeting.builder()
                .user(user) // User 엔터티로 변환
                .title(addUpdateMeetingRequest.getTitle())
                .category(addUpdateMeetingRequest.getCategory())
                .status(0)
                .views(0)
                .applicationCount(0)
                .views(0)
                .applicationDeadline(addUpdateMeetingRequest.getApplicationDeadline())
                .recruitmentCount(addUpdateMeetingRequest.getRecruitmentCount())
                .location(addUpdateMeetingRequest.getLocation())
                .description(addUpdateMeetingRequest.getDescription())
                .creationDate(addUpdateMeetingRequest.getCreationDate())
                .interestLanguage(addUpdateMeetingRequest.getInterestLanguage())
                .interestFramework(addUpdateMeetingRequest.getInterestFramework())
                .interestJob(addUpdateMeetingRequest.getInterestJob())
                .build();
    }
}



