package com.threestar.selectstar.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.dto.user.request.GetUserRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// JWT 인증 필터
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");


    public JwtAuthenticationFilter(JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    private GetUserRequest loginRequest;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // LoginRequest
        ObjectMapper om = new ObjectMapper();

        try {
            loginRequest = om.readValue(request.getInputStream(), GetUserRequest.class);
        } catch (Exception e) {
            log.error("로그인 요청 오류", e);
        }

        log.info("JwtAuthenticationFilter : "+loginRequest);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword());
        log.info("JwtAuthenticationFilter : 토큰생성완료");

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        CustomUserDetails principalDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("user ID : " + principalDetails.getUser().getName());
        log.info("user Password : " + principalDetails.getUser().getPassword());
		return authentication;
    }

    // 사용자가 성공적으로 인증되었을 때 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        log.info("successfulAuthentication");

        // 현재 사용자에 대한 정보 반환
        CustomUserDetails principalDetails = (CustomUserDetails) authentication.getPrincipal();

        // Access Token 발급
        String accessToken = jwtProvider.createAccessToken(principalDetails);

        // Refresh Token 발급
        String refreshToken = jwtProvider.createRefreshToken(principalDetails);

        // Refresh Token을 DB, Cookie에 저장
        jwtProvider.saveRefreshToken(principalDetails.getUser(), refreshToken);
        Cookie cookie = new Cookie("refreshToken", refreshToken);
//        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        response.addHeader("Role", principalDetails.getUser().getRole());
    }

}
