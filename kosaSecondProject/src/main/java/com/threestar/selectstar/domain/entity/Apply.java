package com.threestar.selectstar.domain.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apply {
    @EmbeddedId
    private ApplyID applyID;

    private String emailAddress;
    private String snsAddress;
    private String reason;
    @CreationTimestamp
    private java.sql.Date applicationDate;
    @ColumnDefault("0")
    private int reject;
    private String rejectReason;
}
