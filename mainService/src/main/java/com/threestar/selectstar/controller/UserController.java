package com.threestar.selectstar.controller;

import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.config.jwt.JwtProperties;
import com.threestar.selectstar.config.jwt.JwtProvider;
import com.threestar.selectstar.dto.user.response.GetUserProfileResponse;
import com.threestar.selectstar.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.threestar.selectstar.service.UserService;
import com.threestar.selectstar.dto.user.request.AddUserRequest;
import com.threestar.selectstar.dto.user.request.GetUserRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

	private final UserService userService;
	private final JwtProvider jwtProvider;

	public UserController(UserService userService, JwtProvider jwtProvider) {
		this.userService = userService;
		this.jwtProvider = jwtProvider;
	}

	@Value("${REST_API_KEY}")
	private String apiKey;

	@GetMapping("/users/apiKey")
	public String getApiKey(){
		return apiKey;
	}

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<?> processSignUp (@RequestBody AddUserRequest request) {
		userService.createUser(request);
		return new ResponseEntity<>("회원 가입 성공", HttpStatus.CREATED);
	}

	// 중복확인
	@GetMapping("/users/checkDuplicate")
	public ResponseEntity<?> checkDuplicate(@RequestParam String type, @RequestParam String value){
		try {
			userService.checkDuplicate(type, value);
			if(type.equals("name")){
				return new ResponseEntity<>("사용 가능한 아이디입니다.", HttpStatus.OK);
			}else{
				return new ResponseEntity<>("사용 가능한 닉네임입니다.", HttpStatus.OK);
			}
		}catch (IllegalStateException e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

/*	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> processLogin(@RequestBody GetUserRequest request){
		try {
			User user = userService.loginUser(request);
			log.info("=================로그인==아이디"+user.getName());
			log.info("=================로그인==회원역할"+user.getRole());
			return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
		} catch (IllegalStateException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }*/

	//다른 유저 프로필 조회
	@GetMapping("/profiles/info/{id}")
	@ResponseBody
	public ResponseEntity<?> getUserProfile(@PathVariable int id){
		GetUserProfileResponse res = userService.getUserProfile(id);
		//log.info("res >>"+res);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			userService.deleteRefreshToken(userDetails); // Refresh Token 삭제
			return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("로그아웃 실패", HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/users/validate-access-token")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
		log.info("============="+ token);
		String accessToken = token.replace(JwtProperties.TOKEN_PREFIX, "");
		Map<String, Object> response = new HashMap<>();

		// Access Token 유효성 검사
		if (jwtProvider.isAccessToken(accessToken)) { // Access Token이 유효한 경우
			log.info("===============access token 유효");
			response.put("valid", true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else { // Access Token 만료 또는 유효하지 않은 경우
			log.info("===============access token 만료 또는 유효하지 않음");
			response.put("valid", false);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/users/validate-refresh-token")
	public ResponseEntity<?> validateRefreshToken(@RequestBody Map<String, String> token) {
		String refreshToken = token.get("refreshToken");
		log.info("=================="+refreshToken);
		Map<String, Object> response = new HashMap<>();

		// Refresh Token 유효성 검사
		if (jwtProvider.isRefreshToken(refreshToken)) { // Refresh Token이 유효한 경우
			log.info("===============refresh token 유효");

			// Access Token 재발급
			String newAccessToken = jwtProvider.createAccessTokenFromRefreshToken(refreshToken);

			// 새로운 Access Token을 응답에 포함
			response.put("valid", true);
			response.put("newAccessToken", newAccessToken);
		} else { // Refresh Token 만료 또는 유효하지 않은 경우
			log.info("===============refresh token 만료 또는 유효하지 않음");
			response.put("valid", false);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
