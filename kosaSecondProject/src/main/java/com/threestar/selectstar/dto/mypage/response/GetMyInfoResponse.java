package com.threestar.selectstar.dto.mypage.response;

import com.threestar.selectstar.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class GetMyInfoResponse {
    //이력관리
    private int userId;
    private String name;
    private String aboutMe;
    private String profileContent;

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
