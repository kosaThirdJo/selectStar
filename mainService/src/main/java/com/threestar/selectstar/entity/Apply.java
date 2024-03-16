package com.threestar.selectstar.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
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
    @Setter
    private int applyStatus;
    @Setter
    private String rejectReason;
}
