package com.threestar.selectstar.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    private Integer meetingId;
    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private User user;
    private String title;
    private int category;
    private int status;
    private java.sql.Date applicationDeadline;
    @ColumnDefault("0")
    private int views;
    private int recruitmentCount;
    private int applicationCount;
    private String location;
    private String description;
    @CreationTimestamp
    private java.sql.Date creationDate;
    private String interestLanguage;
    private String interestFramework;
    private String interestJob;
    @ColumnDefault("0")
    private int deleted; // 0:삭제X 1:삭제


}
