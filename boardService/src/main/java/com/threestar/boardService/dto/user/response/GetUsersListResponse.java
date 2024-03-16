package com.threestar.boardService.dto.user.response;

import com.threestar.boardService.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUsersListResponse {
	private long userId;
	private String nickname;
	private String aboutMe;
	private String profilePhoto;
	private String name;
	private String email;
	private int userStatus;
	private Date joinDate;

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
				.name(user.getName())
				.email(user.getEmail())
				.userStatus(user.getUserStatus())
				.joinDate(user.getJoinDate())
				.build();
	}
}
