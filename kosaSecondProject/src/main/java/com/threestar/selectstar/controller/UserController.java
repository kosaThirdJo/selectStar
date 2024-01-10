package com.threestar.selectstar.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.config.jwt.JwtProperties;
import com.threestar.selectstar.domain.entity.User;
import com.threestar.selectstar.dto.user.response.GetUserProfileResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import com.threestar.selectstar.domain.service.UserService;
import com.threestar.selectstar.dto.user.request.AddUserRequest;
import com.threestar.selectstar.dto.user.request.GetUserRequest;


import java.util.Date;
import java.util.Optional;
@Slf4j
@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Value("${REST_API_KEY}")
	private String apiKey;

	@GetMapping("/users/apiKey")
	public String getApiKey(){
		return apiKey;
	}

	// 회원가입
	@PostMapping("/users")
	public ResponseEntity<String> processSignup (@RequestBody AddUserRequest request) {
		try{
			Integer userId = userService.addUser(request);
			return ResponseEntity.ok("회원가입 성공 id: "+userId);
		} catch (IllegalStateException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 중복확인
	@GetMapping("/users/checkDuplicate")
	public ResponseEntity<String> checkDuplicate(@RequestParam String type, @RequestParam String value){
		try {
			userService.checkDuplicate(type, value);
			if(type.equals("name")){
				return ResponseEntity.ok("사용 가능한 아이디입니다.");
			}else{
				return ResponseEntity.ok("사용 가능한 닉네임입니다.");
			}
		} catch (IllegalStateException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<String> processLogin(@RequestBody GetUserRequest request){
		try {
			userService.loginUser(request);
			return ResponseEntity.ok("로그인 성공");
		} catch (Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

/*	@GetMapping("/logout")
	public ResponseEntity<String> processLogout(@RequestHeader(value = "Authorization", required = false) String token, HttpServletResponse response){
		response.addHeader(JwtProperties.HEADER_STRING,"delete");
		return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
	}*/

	//다른 유저 프로필 조회
	@GetMapping("/profiles/info/{id}")
	@ResponseBody
	public ResponseEntity<?> getUserProfile(@PathVariable int id){
		GetUserProfileResponse res = userService.getUserProfile(id);
		//log.info("res >>"+res);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

}
