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
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;

	@Value("${SECRET_KEY}")
	private String SECRET_KEY;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtProvider jwtProvider) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		String header = request.getHeader(JwtProperties.HEADER_STRING);

		if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

		if (jwtProvider.isValidToken(token)) {
			Authentication authentication = getAuthentication(token, response);
			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	}

	private Authentication processAccessToken(String token, HttpServletResponse response) {
		try {
			if (jwtProvider.isAccessToken(token)) {
				return processToken(token, response, true);
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
			processToken(token, response, false);
		}
	}

	private Authentication processToken(String token, HttpServletResponse response, boolean isAccessToken) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
			DecodedJWT decodedJWT = verifier.verify(token);

			Integer userId = decodedJWT.getClaim("userId").asInt();
			if (userId != null) {
				User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
				CustomUserDetails customUserDetails = new CustomUserDetails(user);

				String newToken = jwtProvider.createAccessToken(customUserDetails);

				if (!isAccessToken) {
					newToken = jwtProvider.createAccessTokenFromRefreshToken(token);
				}

				response.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + newToken);

				return new UsernamePasswordAuthenticationToken(
						customUserDetails,
						null,
						customUserDetails.getAuthorities());
			}
		} catch (TokenExpiredException e) {
			log.error("Token이 만료되었습니다.", e);
		} catch (Exception e) {
			log.error("토큰 검증 오류", e);
		}
		return null;
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
}