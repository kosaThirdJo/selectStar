package com.threestar.boardService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("0")
    private int views;

    @CreationTimestamp
    private java.sql.Date creationDate;

    @ColumnDefault("0")
    private int deleted; // 0:삭제X 1:삭제

    public void updateChats(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void deleteChats(int deleted) {
        this.deleted = deleted;
    }
}
