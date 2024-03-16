package com.threestar.boardService.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threestar.boardService.config.auth.CustomUserDetails;
import com.threestar.boardService.dto.user.request.GetUserRequest;
import com.threestar.boardService.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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

    @Autowired
    private UserService userService;
    private GetUserRequest loginRequest;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        // LoginRequest
        ObjectMapper om = new ObjectMapper();

        try {
            loginRequest = om.readValue(request.getInputStream(), GetUserRequest.class);
        } catch (Exception e) {
            log.error("로그인 요청 오류", e);
        }

//        log.info("JwtAuthenticationFilter : "+loginRequest);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword());
//        log.info("JwtAuthenticationFilter : 토큰생성완료");

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 로그인 시도하는 user 상태 조회
        int userStatus = userService.getUserStatus(loginRequest.getName()).getUserStatus();

        // 정지된 사용자 또는 탈퇴한 사용자인 경우
        if (userStatus != 0) {

            // 상태 코드와 메시지를 설정하여 클라이언트에게 전달
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // 메시지
            String message = "";
            if (userStatus == 1) {
                message = "정지된 사용자입니다.";
            } else {
                message = "탈퇴한 사용자입니다.";
            }

            // JSON 형태로 메시지 작성
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(Map.of("message", message));

            // 응답에 JSON 메시지 작성
            PrintWriter writer = response.getWriter();
            writer.println(jsonResponse);
            writer.flush();

            // 인증 실패를 나타내는 객체 반환
            return null;
        }

        /*CustomUserDetails principalDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("user ID : " + principalDetails.getUser().getName());
        log.info("user Password : " + principalDetails.getUser().getPassword());*/

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
