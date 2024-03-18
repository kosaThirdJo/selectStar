package com.threestar.selectstar.dto.mypage.request;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

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
    //포트폴리오 파일
    private MultipartFile profileFile;
    private Long fileId;
    private boolean fileYn;

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

    //탈퇴
    private int userStatus; //0: 활동, 1: 탈퇴, 2: 정지
}
