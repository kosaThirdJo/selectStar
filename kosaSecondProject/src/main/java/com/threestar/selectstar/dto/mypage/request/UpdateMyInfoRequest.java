package com.threestar.selectstar.dto.mypage.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.threestar.selectstar.domain.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMyInfoRequest {
    //이력관리
    private Integer userId;
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

    private byte[] profilePhoto;


}
