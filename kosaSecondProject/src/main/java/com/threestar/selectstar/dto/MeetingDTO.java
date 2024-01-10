package com.threestar.selectstar.dto;


import com.threestar.selectstar.domain.entity.Meeting;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class MeetingDTO {
    private Integer meetingId;
    private UserDTO user;
    private String title;
    private int category;
    private int status;
    private Date applicationDeadline;
    private int views;
    private int recruitmentCount;
    private int applicationCount;
    private String location;
    private String description;
    private Date creationDate;
    private String interestLanguage;
    private String interestFramework;
    private String interestJob;
    private int deleted;


    public static MeetingDTO toDTO(Meeting meeting) {
        return MeetingDTO.builder()
                .meetingId(meeting.getMeetingId())
                .user(UserDTO.toDTO(meeting.getUser())) // UserDto로 변환
                .title(meeting.getTitle())
                .category(meeting.getCategory())
                .status(meeting.getStatus())
                .applicationDeadline(meeting.getApplicationDeadline())
                .views(meeting.getViews())
                .recruitmentCount(meeting.getRecruitmentCount())
                .applicationCount(meeting.getApplicationCount())
                .location(meeting.getLocation())
                .description(meeting.getDescription())
                .creationDate(meeting.getCreationDate())
                .interestLanguage(meeting.getInterestLanguage())
                .interestFramework(meeting.getInterestFramework())
                .interestJob(meeting.getInterestJob())
                .deleted(meeting.getDeleted())
                .build();
    }

    public static Meeting toEntity(MeetingDTO meetingDTO) {
        return Meeting.builder()
                .meetingId(meetingDTO.getMeetingId())
                .user(UserDTO.toEntity(meetingDTO.getUser())) // User 엔터티로 변환
                .title(meetingDTO.getTitle())
                .category(meetingDTO.getCategory())
                .status(meetingDTO.getStatus())
                .applicationDeadline(meetingDTO.getApplicationDeadline())
                .views(meetingDTO.getViews())
                .recruitmentCount(meetingDTO.getRecruitmentCount())
                .applicationCount(meetingDTO.getApplicationCount())
                .location(meetingDTO.getLocation())
                .description(meetingDTO.getDescription())
                .creationDate(meetingDTO.getCreationDate())
                .interestLanguage(meetingDTO.getInterestLanguage())
                .interestFramework(meetingDTO.getInterestFramework())
                .interestJob(meetingDTO.getInterestJob())
                .deleted(meetingDTO.getDeleted())
                .build();
    }
}



