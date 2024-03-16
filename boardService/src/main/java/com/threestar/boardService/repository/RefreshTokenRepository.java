package com.threestar.boardService.repository;


import com.threestar.boardService.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByRefreshToken(String refreshToken);
    RefreshToken findByUser_UserId(Integer user_userId);
}
