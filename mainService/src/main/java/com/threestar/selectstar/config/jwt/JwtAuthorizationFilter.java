package com.threestar.selectstar.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.entity.User;
import com.threestar.selectstar.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
		}else{
			// Access Token이 만료된 경우, Refresh Token을 사용하여 새로운 Access Token 발급
			String refreshToken = extractRefreshTokenFromRequest(request);
			if (refreshToken != null) {
				String newAccessToken = jwtProvider.createAccessTokenFromRefreshToken(refreshToken);

				// 새로운 Access Token을 응답 헤더에 설정
				response.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + newAccessToken);
			}
		}

		chain.doFilter(request, response); // 필터 체인에서 정보 사용
	}

	private Authentication processAccessToken(String token, HttpServletResponse response) {
		try {
			if (jwtProvider.isAccessToken(token)) {
				JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
				DecodedJWT decodedJWT = verifier.verify(token);

				Integer userId = decodedJWT.getClaim("userId").asInt();
				if (userId != null) {
					User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

					// Create CustomUserDetails with the user information
					CustomUserDetails customUserDetails = new CustomUserDetails(user);

					// Create a new Access Token with the updated user information
					String newAccessToken = jwtProvider.createAccessToken(customUserDetails);

					// Set the new Access Token in the response header
					response.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + newAccessToken);

					return new UsernamePasswordAuthenticationToken(
							customUserDetails,
							null,
							customUserDetails.getAuthorities());
				}
			}
		} catch (TokenExpiredException e) {
			log.error("Access Token이 만료되었습니다.", e);
		} catch (Exception e) {
			log.error("토큰 검증 오류", e);
		}
		return null;
	}

	private void processRefreshToken(String token, HttpServletResponse response) {
		if (jwtProvider.isValidToken(token) && jwtProvider.isRefreshToken(token)) {
			String newAccessToken = jwtProvider.createAccessTokenFromRefreshToken(token);
			response.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + newAccessToken);

		}
	}


	private Authentication getAuthentication(String token, HttpServletResponse response) {
		try {
			if (jwtProvider.isAccessToken(token)) {
				return processAccessToken(token, response);
			} else if (jwtProvider.isRefreshToken(token)) {
				processRefreshToken(token, response);
				return null;
			}
		} catch (Exception e) {
			log.error("토큰 검증 오류", e);
		}
		return null;
	}

	private String extractRefreshTokenFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("refreshToken".equals(cookie.getName())) { // refreshToken에 해당하는 쿠키를 찾음
					return cookie.getValue();
				}
			}
		}
		return null; // refreshToken을 찾지 못한 경우
	}

}


