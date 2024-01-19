package com.threestar.selectstar.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "apply")
public class Apply {
    @EmbeddedId
    private ApplyID applyID;
    private String emailAddress;
    private String snsAddress;
    private String reason;
    @CreationTimestamp
    private java.sql.Date applicationDate;
    @ColumnDefault("0")
    private int reject; // 바꿔야 됨
    private int applyStatus;
    private String rejectReason;
}
