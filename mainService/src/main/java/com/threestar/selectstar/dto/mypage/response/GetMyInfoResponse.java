package com.threestar.selectstar.dto.mypage.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@ToString
public class GetMyInfoResponse {
    //이력관리
    private int userId;
    private String name;
    private String aboutMe;
    private String profileContent;
    //이력관리-포트폴리오파일
    //private MultipartFile profileFile;
    private Long fileId;
    private String accessUrl;
    private String originName;

    //개인정보
    private String password;
    private String email;
    private String nickname;
    private String location1;
    private String location2;
    private String interestLanguage;
    private String interestFramework;
    private String interestJob;


    private String profilePhoto;

    //탈퇴여부
    private int userStatus; //0: 활동, 1: 탈퇴, 2: 정지
/*
    //생성자 사용 Entity -> DTO
    public GetMyInfoResponse(User user){
        this.userId = user.getUserId();
        this.name = user.getName();
        this.aboutMe = user.getAboutMe();
        this.profileContent = user.getProfileContent();
    }
*/

}
