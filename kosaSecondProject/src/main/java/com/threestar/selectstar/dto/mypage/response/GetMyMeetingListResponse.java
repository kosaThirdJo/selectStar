package com.threestar.selectstar.dto.mypage.response;

import com.threestar.selectstar.domain.entity.Meeting;
import com.threestar.selectstar.repository.ApplyRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;

@Builder
@ToString
@Getter
public class GetMyMeetingListResponse {
    private int meetingId;
    //private int userId;
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
    //private String interestLanguage;
    //private String interestFramework;
    //private String interestJob;

    private String email;
    private String nickname;
    private byte[] profilePhoto;

    public static GetMyMeetingListResponse fromEntity(Meeting meeting, ApplyRepository applyRepository){
        int applicationCount = applyRepository.countByApplyID_Meeting_MeetingIdIsAndRejectIs(meeting.getMeetingId(),0);
        return GetMyMeetingListResponse.builder()
                .meetingId(meeting.getMeetingId())
                //.userId(meeting.getUser().getUserId())
                .title(meeting.getTitle())
                .category(meeting.getCategory())
                .status(meeting.getStatus())
                .applicationDeadline(meeting.getApplicationDeadline())
                .views(meeting.getViews())
                .recruitmentCount(meeting.getRecruitmentCount())
                .applicationCount(applicationCount)
                //.applicationCount(meeting.getApplicationCount())
                .location(meeting.getLocation())
                .description(meeting.getDescription())
                .creationDate(meeting.getCreationDate())
                .build();
    }
}
