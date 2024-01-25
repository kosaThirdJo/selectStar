package com.threestar.selectstar.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;
    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private User user;
    private int notification_type;
    private String notification_content;
    private String notification_url;
}
