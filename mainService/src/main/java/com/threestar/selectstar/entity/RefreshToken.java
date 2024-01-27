package com.threestar.selectstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @OneToOne
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private User user;

    @Column(name = "refresh_token")
    @NotNull
    private String refreshToken;

    @Column(name = "expire_at")
    private LocalDateTime expireAt;
}