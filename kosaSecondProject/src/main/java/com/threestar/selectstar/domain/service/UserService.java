package com.threestar.selectstar.domain.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.threestar.selectstar.dto.mypage.UserImgFileDTO;
import com.threestar.selectstar.dto.mypage.request.UpdateMyInfoRequest;
import com.threestar.selectstar.dto.mypage.response.GetMyInfoResponse;
import com.threestar.selectstar.dto.user.response.GetUserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.threestar.selectstar.domain.entity.User;
import com.threestar.selectstar.dto.user.request.AddUserRequest;
import com.threestar.selectstar.dto.user.request.GetUserRequest;
import com.threestar.selectstar.dto.user.response.GetUsersListResponse;
import com.threestar.selectstar.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	// 회원 가입
	@Transactional
	public Integer addUser(AddUserRequest request){
		User user = AddUserRequest.toEntity(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole("USER");

		User savedUser = userRepository.save(user);
		return savedUser.getUserId();
	}

	// 중복 확인
	@Transactional(readOnly = true)
	public void checkDuplicate(String type, String value) {
		switch (type) {
			case "name":
				userRepository.findByName(value)
						.ifPresent(u -> {
							throw new IllegalStateException("이미 존재하는 아이디입니다.");
						});
				break;
			case "nickname":
				userRepository.findByNickname(value)
						.ifPresent(u -> {
							throw new IllegalStateException("이미 존재하는 닉네임입니다.");
						});
				break;
		}
	}

	// 로그인
	@Transactional(readOnly = true)
	public void loginUser(GetUserRequest request){
		userRepository.findByNameAndPassword(request.getName(), request.getPassword())
				.ifPresent(u -> {
					throw new IllegalStateException("아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.");
				});
	}

	// 회원 검색
	@Transactional(readOnly = true)
	public List<GetUsersListResponse> searchUser(String searchWord) {
		List<User> searchUser = userRepository.findByNicknameLike("%"+searchWord+"%");

		return searchUser.stream()
			.map(GetUsersListResponse::fromEntity)
			.collect(Collectors.toList());
	}

	//마이페이지 이력관리 조회 요청(UserService 이동 예정)
	public GetMyInfoResponse getMyProfileInfo(int id){
		//Optional : NPE(NullPointerException) 방지 => orElseTrow 사용 개선?
		Optional<User> userO = userRepository.findById(id);
		if(userO.isEmpty()){
			return null;
		}else {
			User userE = userO.get();
			//기본 이미지

			String encodeImg = "";
			byte[] imgByte = userE.getProfilePhoto();
			//유저 이미지 있으면 변환
			if(imgByte != null){
				encodeImg = "data:image/png;base64,"+Base64.getEncoder().encodeToString(imgByte);
			}
			System.out.println("encodeImg >>"+encodeImg);
			return GetMyInfoResponse.builder()
					.userId(id)
					.nickname(userE.getNickname())
					.email(userE.getEmail())
					.profilePhoto(encodeImg)
					.aboutMe(userE.getAboutMe())
					.profileContent(userE.getProfileContent())
					.build();
			//return new GetMyInfoResponse(userE);
		}
	}

	//마이페이지 이력관리 수정 요청
	@jakarta.transaction.Transactional
	public String updateMyProfileInfo(int uId, UpdateMyInfoRequest reqDTO){
		Optional<User> userO = userRepository.findById(uId);
		if(userO.isEmpty()){
			return "찾는 사용자가 없습니다.";
		}else {
			User oldUserEntity = userO.get();
			oldUserEntity.setAboutMe(reqDTO.getAboutMe());
			oldUserEntity.setProfileContent(reqDTO.getProfileContent());
			try {
				userRepository.save(oldUserEntity);
				return "success";
			} catch (Exception e) {
				log.info("update myinfo exception", e.getMessage());
				return e.getMessage();
			}
		}
	}

	//마이페이지 개인정보 조회 요청(UserService 이동 예정)
	public GetMyInfoResponse getMyInfo(int id){
		//Optional : NPE(NullPointerException) 방지
		Optional<User> userO = userRepository.findById(id);
		if(userO.isEmpty()){
			return null;
		}else {
			User userE = userO.get();
			//기본 이미지
			String encodeImg = "/assets/image/global/userdefaultimg.png";
			byte[] imgByte = userE.getProfilePhoto();
			//유저 이미지 있으면 변환
			if(imgByte != null){
				encodeImg = "data:image/png;base64,"+Base64.getEncoder().encodeToString(imgByte);
			}
			System.out.println("encodeImg >>"+encodeImg);
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

	//마이페이지 개인정보 수정 요청(UserService 이동 예정)
	@jakarta.transaction.Transactional
	public String updateMyInfo(int uId, UpdateMyInfoRequest reqDTO){
		Optional<User> userO = userRepository.findById(uId);
		if(userO.isEmpty()){
			return "찾는 사용자가 없습니다.";
		}else {
			User oldUserEntity = userO.get();
			//oldUserEntity.setPassword(reqDTO.getPassword());
			oldUserEntity.setPassword(passwordEncoder.encode(reqDTO.getPassword()));
			oldUserEntity.setEmail(reqDTO.getEmail());
			oldUserEntity.setNickname(reqDTO.getNickname());
			oldUserEntity.setLocation1(reqDTO.getLocation1());
			oldUserEntity.setLocation2(reqDTO.getLocation2());
			oldUserEntity.setInterestLanguage(reqDTO.getInterestLanguage());
			oldUserEntity.setInterestFramework(reqDTO.getInterestFramework());
			oldUserEntity.setInterestJob(reqDTO.getInterestJob());
			try {
				userRepository.save(oldUserEntity);
				return "success";
			} catch (Exception e) {
				log.info("update myinfo exception", e.getMessage());
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
				oldUserE.setProfilePhoto(byteImg);
				return "success";
			}catch (Exception e){
				log.info("update profile img error"+e.getMessage());
				return e.getMessage();
			}
		}
	}

	//다른 유저 프로필 조회
	public GetUserProfileResponse getUserProfile(int id){
		Optional<User> userO = userRepository.findById(id);
		if(userO.isEmpty()){
			return null;
		}else{
			User userE = userO.get();
			//기본 이미지
			String encodeImg = "";
			byte[] imgByte = userE.getProfilePhoto();
			//유저 이미지 있으면 변환
			if(imgByte != null){
				encodeImg = "data:image/png;base64,"+ Base64.getEncoder().encodeToString(imgByte);
			}
			System.out.println("encodeImg >>"+encodeImg);
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

}
