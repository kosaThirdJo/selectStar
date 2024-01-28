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
import org.springframework.stereotype.Component;

import java.util.Date;
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
        RefreshToken newRefreshToken = RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(newRefreshToken);
    }

    public boolean isAccessToken(String token) {
        return isValidToken(token) && Objects.equals(extractClaimType(token), "access") && isTokenExpired(token);
    }


    public boolean isRefreshToken(String token) {
        return isValidToken(token) && Objects.equals(extractClaimType(token), "refresh") && isTokenExpired(token);
    }


    public boolean isValidToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("=============Exception in isValidTokenMethod", e);
            return false;
        }
    }


    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
            Date expiration = decodedJWT.getExpiresAt();
            log.info("=============expiration: " + expiration);
            return expiration != null && expiration.after(new Date()); // 만료 시간이 존재하고 현재 시간보다 미래인 경우 true 반환

        } catch (Exception e) { // 토큰 디코드 실패 또는 만료 시간이 없는 경우
            log.error("=============Exception in isTokenExpiredMethod", e);
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

}
