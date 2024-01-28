package com.threestar.selectstar.controller;

import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.config.jwt.JwtProperties;
import com.threestar.selectstar.config.jwt.JwtProvider;
import com.threestar.selectstar.dto.user.response.GetUserProfileResponse;
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
		return new ResponseEntity<>("success", HttpStatus.CREATED);
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
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<String> processLogin(@RequestBody GetUserRequest request){
		try {
			userService.loginUser(request);
			return ResponseEntity.ok("로그인 성공");
		} catch (IllegalStateException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

	//다른 유저 프로필 조회
	@GetMapping("/profiles/info/{id}")
	@ResponseBody
	public ResponseEntity<?> getUserProfile(@PathVariable int id){
		GetUserProfileResponse res = userService.getUserProfile(id);
		//log.info("res >>"+res);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			userService.deleteRefreshToken(userDetails); // Refresh Token 삭제
			return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("로그아웃 실패", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/users/validate-token")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("============="+ token);
		String accessToken = token.replace(JwtProperties.TOKEN_PREFIX, "");

		// Access Token 유효성 검사
		if (jwtProvider.isAccessToken(accessToken)){
			log.info("===============access token 유효");
			Map<String, Object> response = new HashMap<>();
			response.put("valid", true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

//		// Access Token 유효성 검사
//		if (jwtProvider.isAccessToken(token)) {
//			// Access Token이 유효한 경우
//			log.info("===============access token 유효");
//			Map<String, Object> response = new HashMap<>();
//			response.put("valid", true);
//			return ResponseEntity.ok(response);
//		} else {
//			log.info("===============access token 만료");
//			// Access Token이 만료된 경우
//			String refreshToken = getRefreshToken(userDetails);
//			if (refreshToken != null && jwtProvider.isRefreshToken(refreshToken)) {
//				// Refresh Token이 유효한 경우
//				log.info("===============refresh token 유효");
//				String newAccessToken = jwtProvider.createAccessTokenFromRefreshToken(refreshToken);
//
//				if (newAccessToken != null) {
//					// 새로운 Access Token을 생성하여 반환
//					Map<String, Object> response = new HashMap<>();
//					response.put("valid", true);
//					response.put("newAccessToken", newAccessToken);
//					return ResponseEntity.ok(response);
//				}
//			}
//		}
//
//		// Access Token 및 Refresh Token이 유효하지 않은 경우
//		Map<String, Object> response = new HashMap<>();
//		response.put("valid", false);
//		return ResponseEntity.ok(response);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	private String getRefreshToken(CustomUserDetails userDetails) {
		return userService.getRefreshToken(userDetails);
	}
}
