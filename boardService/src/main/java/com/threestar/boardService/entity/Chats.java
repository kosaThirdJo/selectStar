package com.threestar.boardService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Chats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer meetingId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="userId", referencedColumnName = "userId")
//    private User user;

    private String title;

    private int status;

    private java.sql.Date applicationDeadline;

    @ColumnDefault("0")
    private int views;

    private String description;

    @CreationTimestamp
    private java.sql.Date creationDate;

    @ColumnDefault("0")
    private int deleted; // 0:삭제X 1:삭제
}
