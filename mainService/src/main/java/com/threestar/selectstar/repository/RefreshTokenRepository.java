package com.threestar.selectstar.repository;

import com.threestar.selectstar.entity.RefreshToken;
import com.threestar.selectstar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByRefreshToken(String refreshToken);
    List<RefreshToken> findByExpireAtBefore(LocalDateTime currentDateTime);
    RefreshToken findByUser_UserId(Integer user_userId);
}
