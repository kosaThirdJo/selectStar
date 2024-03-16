package com.threestar.selectstar.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meeting_mark")
@Entity
public class MeetingMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meeting_mark_id;
    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private User users;
    @ManyToOne
    @JoinColumn(name="meetingId", referencedColumnName = "meetingId")
    private Meeting meeting;


}
