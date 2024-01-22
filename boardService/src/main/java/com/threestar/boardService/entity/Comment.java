package com.threestar.boardService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private Long commentId;

    // private User user;

    // private Chats chats;
    @Column(nullable = false, length = 255)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    private int depth;

    @CreationTimestamp
    private java.sql.Date creationDate;

    @ColumnDefault("0")
    private int deleted; // 0:삭제X 1:삭제

}
