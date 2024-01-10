package com.threestar.selectstar.dto.user.request;

import java.sql.Date;

import com.threestar.selectstar.domain.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {
	private String name;
	private String password;
	private String email;
	private String nickname;
	private String location1;
	private Date joinDate;
	private String interestLanguage;
	private String interestFramework;
	private String interestJob;

	public static User toEntity(AddUserRequest request){
		return User.builder()
			.name(request.getName())
			.password(request.getPassword())
			.email(request.getEmail())
			.nickname(request.getNickname())
			.location1(request.getLocation1())
			.joinDate(request.getJoinDate())
			.interestLanguage(request.getInterestLanguage())
			.interestFramework(request.getInterestFramework())
			.interestJob(request.getInterestJob())
			.build();
	}
}
