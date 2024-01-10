package com.threestar.selectstar.dto.user.response;

import com.threestar.selectstar.domain.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUsersListResponse {
	private int userId;
	private String nickname;
	private String aboutMe;
	private String profilePhoto;

	public static GetUsersListResponse fromEntity(User user){
		String encodeImg = "";
		byte[] imgByte = user.getProfilePhoto();

		if (user.getProfilePhoto() != null) {
			encodeImg = "data:image/png;base64," + Base64.getEncoder().encodeToString(imgByte);
		}

		return GetUsersListResponse.builder()
				.userId(user.getUserId())
				.nickname(user.getNickname())
				.aboutMe(user.getAboutMe())
				.profilePhoto(encodeImg)
				.build();
	}
}
