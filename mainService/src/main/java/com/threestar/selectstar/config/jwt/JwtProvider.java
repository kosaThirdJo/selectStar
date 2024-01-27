package com.threestar.selectstar.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.entity.RefreshToken;
import com.threestar.selectstar.entity.User;
import com.threestar.selectstar.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class JwtProvider {
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtProvider(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createAccessToken(CustomUserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("userId", userDetails.getUser().getUserId())
                .withClaim("type", "access")
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createRefreshToken(CustomUserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .withClaim("userId", userDetails.getUser().getUserId())
                .withClaim("type", "refresh")
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public void saveRefreshToken(User user, String refreshToken) {
        RefreshToken existingRefreshToken = refreshTokenRepository.findByUser_UserId(user.getUserId());  // 기존에 저장된 Refresh Token이 있는지 확인

        // 기존 Refresh Token이 있으면 삭제
        if (existingRefreshToken != null) {
            refreshTokenRepository.delete(existingRefreshToken);
        }

        // 새로운 Refresh Token 저장
        LocalDateTime expireAt = LocalDateTime.now().plus(Duration.ofMillis(JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME));

        RefreshToken newRefreshToken = RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expireAt(expireAt)
                .build();

        refreshTokenRepository.save(newRefreshToken);
    }

    public boolean isAccessToken(String token) {
        return isValidToken(token) && Objects.equals(extractClaimType(token), "access");
    }

    public boolean isRefreshToken(String token) {
        return isValidToken(token) && Objects.equals(extractClaimType(token), "refresh");
    }

    public boolean isValidToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String extractClaimType(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
            return decodedJWT.getClaim("type").asString();
        } catch (Exception e) {
            return null;
        }
    }

    public String createAccessTokenFromRefreshToken(String refreshToken) {
        if (isRefreshToken(refreshToken)) {
            RefreshToken storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (storedRefreshToken != null) {
                User user = storedRefreshToken.getUser();
                CustomUserDetails userDetails = new CustomUserDetails(user);

                return createAccessToken(userDetails);
            }
        }
        return null;
    }

    @Scheduled(fixedRate = 86400000) // 1일마다 실행
    public void deleteExpiredRefreshTokens() {
        // 현재 시간 기준으로 만료된 refresh token을 DB에서 삭제
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<RefreshToken> expiredRefreshTokens = refreshTokenRepository.findByExpireAtBefore(currentDateTime);
        refreshTokenRepository.deleteAll(expiredRefreshTokens);
    }

}
