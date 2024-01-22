package com.threestar.selectstar.controller;

import com.threestar.selectstar.dto.user.response.GetUserProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.threestar.selectstar.service.UserService;
import com.threestar.selectstar.dto.user.request.AddUserRequest;
import com.threestar.selectstar.dto.user.request.GetUserRequest;

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

}
