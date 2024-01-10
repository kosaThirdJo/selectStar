package com.threestar.selectstar.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.domain.entity.User;
import com.threestar.selectstar.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;


// JWT 인가 필터
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	
	private final UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Value("${SECRET_KEY}")
	private String SECRET_KEY;

	// doFilterInternal : Spring Security에서 제공하는 OncePerRequestFilter 클래스의 메소드
	// HTTP 요청이 들어올 때마다 한 번씩 호출되며, 필터의 실제 로직을 수행
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		// HTTP 요청 헤더에서 JWT 토큰을 가져옴
		String header = request.getHeader(JwtProperties.HEADER_STRING);

		// 토큰이 없거나, JWT 형식에 맞지 않은 경우
		if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)){
			chain.doFilter(request, response);
			return;
		}
		System.out.println("header : "+header);

		// 토큰
		String token = header.replace(JwtProperties.TOKEN_PREFIX, "");

		// 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
		// 내가 SecurityContext에 직접 접근해서 세션을 만들때, 자동으로 UserDetailsService에 있는 loadByUsername이 호출됨.
		// JWT 토큰을 검증하고, 검증이 성공하면 토큰에서 추출한 사용자 아이디를 사용하여 데이터베이스에서 해당 사용자 정보를 가져옴
		String username = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token).getClaim("sub").asString();

		//token에서 id로 받아오기
		Integer userId = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token).getClaim("userId").asInt();
		System.out.println(userId);
		if(userId != null){
			User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
			CustomUserDetails customUserDetails = new CustomUserDetails(user);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					customUserDetails,
					null,
					customUserDetails.getAuthorities());

			System.out.println("확인");
			System.out.println(authentication);
			// 강제로 시큐리티의 세션에 접근하여 값 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}
		/*
		if(username != null){
			User user = userRepository.findByName(username);

			// 인증은 토큰 검증시 끝.
			// 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
			// 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장

			// 검증이 완료되면, 해당 사용자 정보를 기반으로 Spring Security의 Authentication 객체를 생성하고, 이를 사용하여 사용자를 인증
			// 그리고 이 인증 정보를 SecurityContextHolder를 통해 세션에 저장
			CustomUserDetails customUserDetails = new CustomUserDetails(user);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					customUserDetails,
					null,
					customUserDetails.getAuthorities());

			System.out.println("확인");
			System.out.println(authentication);
			// 강제로 시큐리티의 세션에 접근하여 값 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		*/

		// 필터 체인에서 정보 사용
		chain.doFilter(request, response);
	}


}
