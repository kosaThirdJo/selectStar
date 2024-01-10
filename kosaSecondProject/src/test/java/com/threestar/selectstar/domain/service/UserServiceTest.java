package com.threestar.selectstar.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.threestar.selectstar.dto.user.request.AddUserRequest;
import com.threestar.selectstar.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Test
	void 회원가입() {
		// Given
		AddUserRequest newUser = AddUserRequest.builder()
			.name("testid12345")
			.password("qwer1234")
			.email("test@gmail.com")
			.nickname("test")
			.location1("경기도")
			.joinDate(Date.valueOf(LocalDate.now()))
			.interestLanguage("")
			.interestJob("_4_")
			.interestFramework("_2_")
			.build();

		// When
		int result = userService.addUser(newUser);

		// Then
		assertThat(result).isNotNull();

	}
}
