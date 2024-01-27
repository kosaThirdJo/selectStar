package com.threestar.selectstar.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.entity.User;
import com.threestar.selectstar.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

// JWT 인가 필터
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtProvider jwtProvider) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.jwtProvider = jwtProvider;
	}

	@Value("${SECRET_KEY}")
	private String SECRET_KEY;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		String header = request.getHeader(JwtProperties.HEADER_STRING); // HTTP 요청 헤더에서 JWT 토큰을 가져옴

		if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)){ // 토큰이 없거나, JWT 형식에 맞지 않은 경우
			chain.doFilter(request, response);
			return;
		}

		// 토큰
		String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

		// 토큰 검증
		if (jwtProvider.isValidToken(token)) {
			Authentication authentication = getAuthentication(token, response);
			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response); // 필터 체인에서 정보 사용
	}

	private Authentication processAccessToken(String token) {
		if (jwtProvider.isValidToken(token)) {
			Integer userId = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token).getClaim("userId").asInt();
			if (userId != null) {
				User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
				CustomUserDetails customUserDetails = new CustomUserDetails(user);
				return new UsernamePasswordAuthenticationToken(
						customUserDetails,
						null,
						customUserDetails.getAuthorities());
			}
		}
		return null;
	}
	private void processRefreshToken(String token, HttpServletResponse response) {
		if (jwtProvider.isValidToken(token) && jwtProvider.isRefreshToken(token)) {
			String newAccessToken = jwtProvider.createAccessTokenFromRefreshToken(token);
			response.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + newAccessToken);

			jwtProvider.deleteExpiredRefreshTokens(); // 삭제된 refresh token은 다시 DB에서 삭제
		}
	}


	private Authentication getAuthentication(String token, HttpServletResponse response) {
		try {
			if (jwtProvider.isAccessToken(token)) {
				return processAccessToken(token);
			} else if (jwtProvider.isRefreshToken(token)) {
				processRefreshToken(token, response);
				return null;
			}
		} catch (Exception e) {
			log.error("토큰 검증 오류", e);
		}
		return null;
	}
}


