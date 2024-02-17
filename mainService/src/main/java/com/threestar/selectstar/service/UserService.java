package com.threestar.selectstar.service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.dto.mypage.UserImgFileDTO;
import com.threestar.selectstar.dto.mypage.request.UpdateMyInfoRequest;
import com.threestar.selectstar.dto.mypage.response.GetMyInfoResponse;
import com.threestar.selectstar.dto.user.response.GetUserProfileResponse;
import com.threestar.selectstar.entity.RefreshToken;
import com.threestar.selectstar.entity.Portfolio;
import com.threestar.selectstar.entity.User;
import com.threestar.selectstar.repository.RefreshTokenRepository;
import com.threestar.selectstar.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.threestar.selectstar.dto.user.request.AddUserRequest;
import com.threestar.selectstar.dto.user.request.GetUserRequest;
import com.threestar.selectstar.dto.user.response.GetUsersListResponse;
import com.threestar.selectstar.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //포트폴리오 파일
    private final PortfolioRepository portfolioRepository;
    private final PortfolioService portfolioService;

    // 회원 가입
    @Transactional
    public void createUser(AddUserRequest request) {
        User user = AddUserRequest.toEntity(request);
        user.updatePassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    // 중복 확인
    @Transactional(readOnly = true)
    public void checkDuplicate(String type, String value) {
        switch (type) {
            case "name":
                userRepository.findByName(value).ifPresent(existId -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
                break;
            case "nickname":
                userRepository.findByNickname(value).ifPresent(existNickname -> {
                    throw new IllegalStateException("이미 존재하는 닉네임입니다.");
                });
                break;
        }
    }

    // 로그인
    @Transactional(readOnly = true)
    public User loginUser(GetUserRequest request) {
        return userRepository.findByNameAndPassword(request.getName(), request.getPassword())
                .orElseThrow(() -> new IllegalStateException("아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요."));
    }


    // 회원 검색
    @Transactional(readOnly = true)
    public List<GetUsersListResponse> searchUser(String searchWord) {
        List<User> searchUsers = userRepository.findByNicknameLike("%" + searchWord + "%");

        return searchUsers.stream()
                .map(GetUsersListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //마이페이지 이력관리 조회 요청
    public GetMyInfoResponse getMyProfileInfo(int id) {
        //Optional : NPE(NullPointerException) 방지 => orElseTrow 사용 개선?
        Optional<User> userO = userRepository.findById(id);
        if (userO.isEmpty()) {
            return null;
        } else {
            User userE = userO.get();
            //기본 이미지

            String encodeImg = "";
            byte[] imgByte = userE.getProfilePhoto();
            //유저 이미지 있으면 변환
            if (imgByte != null) {
                encodeImg = "data:image/png;base64," + Base64.getEncoder().encodeToString(imgByte);
            }
            System.out.println("encodeImg >>" + encodeImg);
            return GetMyInfoResponse.builder()
                    .userId(id)
                    .nickname(userE.getNickname())
                    .email(userE.getEmail())
                    .profilePhoto(encodeImg)
                    .aboutMe(userE.getAboutMe())
                    .profileContent(userE.getProfileContent())
                    .profileFile(null)
                    .build();
            //return new GetMyInfoResponse(userE);
        }
    }

    //마이페이지 이력관리 수정 요청
    @jakarta.transaction.Transactional
    public String updateMyProfileInfo(int uId, UpdateMyInfoRequest reqDTO) {
        Optional<User> userO = userRepository.findById(uId);
        if (userO.isEmpty()) {
            return "찾는 사용자가 없습니다.";
        } else {
            User oldUserEntity = userO.get();
            oldUserEntity.updateMyIProfile(reqDTO.getAboutMe(), reqDTO.getProfileContent());
            //첨부파일
            Optional<Portfolio> portfolioOptional = portfolioRepository.findByUser(oldUserEntity);
            if(portfolioOptional.isEmpty()){//기존 포폴파일 없을 경우
                //새로 추가
                portfolioService.savePortfolioFile(oldUserEntity, reqDTO.getProfileFile());
            }else{
                //기존 포폴 삭제 후 저장
            }
            try {
                userRepository.save(oldUserEntity);
                return "success";
            } catch (Exception e) {
                log.info("update myinfo exception", e.getMessage());
                return e.getMessage();
            }
        }
    }

    //마이페이지 개인정보 조회 요청
    public GetMyInfoResponse getMyInfo(int id) {
        //Optional : NPE(NullPointerException) 방지
        Optional<User> userO = userRepository.findById(id);
        if (userO.isEmpty()) {
            return null;
        } else {
            User userE = userO.get();
            //기본 이미지
            String encodeImg = "/assets/image/global/userdefaultimg.png";
            byte[] imgByte = userE.getProfilePhoto();
            //유저 이미지 있으면 변환
            if (imgByte != null) {
                encodeImg = "data:image/png;base64," + Base64.getEncoder().encodeToString(imgByte);
            }
            System.out.println("encodeImg >>" + encodeImg);
            return GetMyInfoResponse.builder()
                    .userId(id)
                    //.name(userE.getName())
                    .password(userE.getPassword())
                    .email(userE.getEmail())
                    .nickname(userE.getNickname())
                    //.profilePhoto(encodeImg)
                    .location1(userE.getLocation1())
                    .location2(userE.getLocation2())
                    .interestLanguage(userE.getInterestLanguage())
                    .interestFramework(userE.getInterestFramework())
                    .interestJob(userE.getInterestJob())
                    .build();
            //return new GetMyInfoResponse(userE);
        }
    }

	//마이페이지 개인정보 수정 요청
	@jakarta.transaction.Transactional
	public String updateMyInfo(int uId, UpdateMyInfoRequest reqDTO){
		Optional<User> userO = userRepository.findById(uId);
		if(userO.isEmpty()){
			return "찾는 사용자가 없습니다.";
		}else {
			User oldUserEntity = userO.get();
			//oldUserEntity.setPassword(reqDTO.getPassword());
            oldUserEntity.updateMyInfo(passwordEncoder.encode(reqDTO.getPassword()), reqDTO.getEmail(),
                    reqDTO.getNickname(), reqDTO.getLocation1(), reqDTO.getLocation2(),reqDTO.getInterestLanguage(),
                    reqDTO.getInterestFramework(), reqDTO.getInterestJob()) ;
			try {
				userRepository.save(oldUserEntity);
				return "success";
			} catch (Exception e) {
				log.info("update myinfo exception", e.getMessage());
				return e.getMessage();
			}
		}
	}
    //마이페이지(개인정보수정)-회원 탈퇴
    @jakarta.transaction.Transactional
    public String updateUserStatus(int uId){
        Optional<User> userO = userRepository.findById(uId);
        if(userO.isEmpty()){
            return "찾는 사용자가 없습니다.";
        }else {
            User oldUserEntity = userO.get();
            oldUserEntity.updateUserStatus(1);
            try {
                userRepository.save(oldUserEntity);
                return "success";
            } catch (Exception e) {
                log.info("update user status exception", e.getMessage());
                return e.getMessage();
            }
        }
    }

	//프로필 이미지 수정
	@jakarta.transaction.Transactional
	public String updateMyProfileImg(int uId, UserImgFileDTO fileDTO){
		Optional<User> userO = userRepository.findById(uId);
		if(userO.isEmpty()){
			return "찾는 사용자가 없습니다.";
		}else {
			User oldUserE = userO.get();
			byte[] byteImg = null;
			try {
				byteImg = fileDTO.getProfilePhoto().getBytes();
				//oldUserE.setProfilePhoto(byteImg);
                oldUserE.updateProfilePhoto(byteImg);
				return "success";
			}catch (Exception e){
				log.info("update profile img error"+e.getMessage());
				return e.getMessage();
			}
		}
	}

    //다른 유저 프로필 조회
    public GetUserProfileResponse getUserProfile(int id) {
        Optional<User> userO = userRepository.findById(id);
        if (userO.isEmpty()) {
            return null;
        } else {
            User userE = userO.get();
            //기본 이미지
            String encodeImg = "";
            byte[] imgByte = userE.getProfilePhoto();
            //유저 이미지 있으면 변환
            if (imgByte != null) {
                encodeImg = "data:image/png;base64," + Base64.getEncoder().encodeToString(imgByte);
            }
            System.out.println("encodeImg >>" + encodeImg);
            return GetUserProfileResponse.builder()
                    .userId(id)
                    .name(userE.getName())
                    .nickname(userE.getNickname())
                    .email(userE.getEmail())
                    .profilePhoto(encodeImg)
                    .aboutMe(userE.getAboutMe())
                    .profileContent(userE.getProfileContent())
                    .build();
        }
    }

    @Transactional
    public void deleteRefreshToken(CustomUserDetails userDetails) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser_UserId(userDetails.getUserId());
        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);
        }
    }

}
