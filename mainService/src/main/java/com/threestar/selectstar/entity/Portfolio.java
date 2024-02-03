package com.threestar.selectstar.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long fileId;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)   //User 엔티티 데이터 삭제 시 같이 삭제
    private User user;

    @Column(name = "access_url")
    private String accessUrl; //s3 내부 이미지에 접근할 수 있는 URL

    @Column(name = "origin_name")
    private String originName; //이미지 파일 본래 이름

    @Column(name = "stored_name")
    private String storedName; //이미지 파일이 S3에 저장될 때 사용되는 이름
}
