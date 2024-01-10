package com.threestar.selectstar.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserImgFileDTO {
    private MultipartFile profilePhoto;
}
