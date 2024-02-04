package com.threestar.selectstar.entity;

import com.threestar.selectstar.dto.meeting.request.AddUpdateMeetingRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


//TODO 댓글 수정 삭제
@Getter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
    @ManyToOne
    @JoinColumn(name="meetingId")
    private Meeting meeting;
    @Setter
    private String content;
    @CreationTimestamp
    private java.sql.Date creationDate;
    @Setter
    private int deleted; // 0:삭제X 1:삭제




}
