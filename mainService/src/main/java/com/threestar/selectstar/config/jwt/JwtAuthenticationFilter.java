package com.threestar.selectstar.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.dto.user.request.GetUserRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Date;


// JWT 인증 필터
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final AuthenticationManager authenticationManager;
	// login 경로에 대한 POST 요청만을 처리하도록 지정된 AntPathRequestMatcher
	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login","POST");

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
		System.out.println("authenticationManager 객체 생성");
		this.authenticationManager = authenticationManager;
	}

	@Value("${SECRET_KEY}")
	private String SECRET_KEY;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		System.out.println("JwtAuthenticationFilter : 진입");

		// 클라이언트로부터 전송된 JSON 데이터를 GetUserRequest 객체로 매핑
		// request의 username, password를 파싱해서 입력해서 반환
		ObjectMapper om = new ObjectMapper();
		GetUserRequest getUserRequest = null;
		try {
			getUserRequest = om.readValue(request.getInputStream(), GetUserRequest.class);
		} catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("JwtAuthenticationFilter : "+getUserRequest);

		// UsernamePasswordAuthenticationToken 생성
		// 요청으로 준 유저정보와 DB에 담긴 유저정보가 같은지를 판별하기 위한 토큰 (사용자의 인증을 시도하는 토큰)
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(getUserRequest.getName(), getUserRequest.getPassword());
		System.out.println("JwtAuthenticationFilter : 토큰생성완료");

		// authenticate() 함수가 호출 되면 AuthenticationProvider가 UserDetailsService 객체의
		// loadUserByUsername(토큰의 첫 번째 파라미터 값) 를 호출하고
		// UserDetails를 리턴받아서 토큰의 두 번째 파라미터(credential)값과
		// UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
		// Authentication 객체를 만들어서 필터체인으로 리턴해준다.

		// Tip: AuthenticationProvider의 디폴트 서비스는 UserDetailsService 타입
		// Tip: AuthenticationProvider의 디폴트 암호화 방식은 BCryptPasswordEncoder 타입
		// 결론은 인증 프로바이더에게 알려줄 필요가 없음
		// 인증 작업
		try {
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			CustomUserDetails principalDetails = (CustomUserDetails) authentication.getPrincipal();
			System.out.println("Authentication: " + principalDetails.getUser().getName());
			return authentication;
		} catch (AuthenticationException e) {
			System.out.println("Authentication failed: " + e.getMessage());
			throw e;
		}
	}

	// 사용자가 성공적으로 인증되었을 때 실행
	// attemptAuthentication()의 호출 결과로 Authentication 객체 리턴시 successfulAuthentication()의 호출 결과를 리턴함
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
		System.out.println("successfulAuthentication");

		// 현재 사용자에 대한 정보 반환
		CustomUserDetails principalDetails = (CustomUserDetails) authentication.getPrincipal();

		// JWT Token 생성해서 response에 담아줌
		// JWT Token : 사용자의 인증이 성공했을 때 클라이언트에게 반환되는 토큰
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())  //  사용자의 식별 정보
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))  // 토큰 유효기간 (현재시간 + 토큰 기간)
				.withClaim("userId", principalDetails.getUser().getUserId())  //  사용자 정의 클레임 - userId(사용자 번호) 추가함 (토큰을 검증할 때 사용할 수 있음)
				.sign(Algorithm.HMAC512(SECRET_KEY));  // 서명 단계 (지정된 알고리즘과 시크릿 키 사용)



		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);  // 생성된 JWT 토큰을 HTTP 응답 헤더에 추가
	}



}
