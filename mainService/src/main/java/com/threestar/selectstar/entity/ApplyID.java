package com.threestar.selectstar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ApplyID implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingId")
    private Meeting meeting;
}
