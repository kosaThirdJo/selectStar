package com.threestar.selectstar.dto;

import com.threestar.selectstar.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
@Builder
public class UserDTO {
    private int userId;
    private String name;
    private String password;
    private String email;
    private String nickname;
    private String location1;
    private String location2;
    private Date joinDate;
    private byte[] profilePhoto;
    private String aboutMe;
    private String profileContent;
    private String interestLanguage;
    private String interestFramework;
    private String interestJob;
    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .location1(user.getLocation1())
                .location2(user.getLocation2())
                .joinDate(user.getJoinDate())
                .profilePhoto(user.getProfilePhoto())
                .aboutMe(user.getAboutMe())
                .profileContent(user.getProfileContent())
                .interestLanguage(user.getInterestLanguage())
                .interestFramework(user.getInterestFramework())
                .interestJob(user.getInterestJob())
                .build();
    }
    public static User toEntity(UserDTO userDTO){
        return User.builder()
                .userId(userDTO.getUserId())
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .nickname(userDTO.getNickname())
                .location1(userDTO.getLocation1())
                .location2(userDTO.getLocation2())
                .joinDate(userDTO.getJoinDate())
                .profilePhoto(userDTO.getProfilePhoto())
                .aboutMe(userDTO.getAboutMe())
                .profileContent(userDTO.getProfileContent())
                .interestLanguage(userDTO.getInterestLanguage())
                .interestFramework(userDTO.getInterestFramework())
                .interestJob(userDTO.getInterestJob())
                .build();
    }
}
