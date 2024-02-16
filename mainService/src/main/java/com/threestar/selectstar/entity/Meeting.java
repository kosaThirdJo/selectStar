package com.threestar.selectstar.entity;

import com.threestar.selectstar.dto.meeting.request.AddUpdateMeetingRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meeting")
@Builder
public class Meeting {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return category == meeting.category && status == meeting.status && views == meeting.views && recruitmentCount == meeting.recruitmentCount && applicationCount == meeting.applicationCount && deleted == meeting.deleted && Objects.equals(meetingId, meeting.meetingId) && Objects.equals(user, meeting.user) && Objects.equals(title, meeting.title) && Objects.equals(location, meeting.location) && Objects.equals(description, meeting.description) && Objects.equals(interestLanguage, meeting.interestLanguage) && Objects.equals(interestFramework, meeting.interestFramework) && Objects.equals(interestJob, meeting.interestJob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingId, user, title, category, status, views, recruitmentCount, applicationCount, location, description, interestLanguage, interestFramework, interestJob, deleted);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId;
    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private User user;
    private String title;
    private int category;
    @Setter
    private int status;
    private Date applicationDeadline;
    @ColumnDefault("0")
    @Setter
    private int views;
    private int recruitmentCount;
    private int applicationCount;
    private String location;
    private String description;
    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    private Date updateDate  = Date.valueOf(LocalDate.now());
    @CreationTimestamp
    private Date creationDate;
    private String interestLanguage;
    private String interestFramework;
    private String interestJob;
    @ColumnDefault("0")
    @Setter
    private int deleted; // 0:삭제X 1:삭제

    public void updateMeeting(AddUpdateMeetingRequest updateRequest) {
        this.title = updateRequest.getTitle();
        this.category = updateRequest.getCategory();
        this.applicationDeadline = updateRequest.getApplicationDeadline();
        this.location = updateRequest.getLocation();
        this.recruitmentCount = updateRequest.getRecruitmentCount();
        this.interestLanguage = updateRequest.getInterestLanguage();
        this.interestFramework = updateRequest.getInterestFramework();
        this.interestJob = updateRequest.getInterestJob();
        this.description = updateRequest.getDescription();
    }
}
