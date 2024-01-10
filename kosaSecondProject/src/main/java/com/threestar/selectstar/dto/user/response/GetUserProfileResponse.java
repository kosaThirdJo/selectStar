package com.threestar.selectstar.dto.user.response;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserProfileResponse {
    //다른 유저의 이력 확인
    private int userId;
    private String name;
    private String nickname;
    private String email;
    private String profilePhoto;
    private String aboutMe;
    private String profileContent;

}
