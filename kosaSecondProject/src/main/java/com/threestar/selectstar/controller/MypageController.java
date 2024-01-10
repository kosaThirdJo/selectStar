package com.threestar.selectstar.controller;


import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.domain.service.MeetingService;
import com.threestar.selectstar.domain.service.UserService;
import com.threestar.selectstar.dto.mypage.*;
import com.threestar.selectstar.dto.mypage.request.UpdateMyInfoRequest;
import com.threestar.selectstar.dto.mypage.response.GetMyApplyingListResponse;
import com.threestar.selectstar.dto.mypage.response.GetMyInfoResponse;
import com.threestar.selectstar.dto.mypage.response.GetMyMeetingListResponse;
import com.threestar.selectstar.exception.MeetingNotFoundException;
import com.threestar.selectstar.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
@Controller
public class MypageController {

    private final UserService userService;
    private final MeetingService meetingService;

    //마이페이지-이력관리 조회
    @GetMapping("/users/profile")
    @ResponseBody
    public ResponseEntity<GetMyInfoResponse> getMyProfileInfo(@AuthenticationPrincipal CustomUserDetails userDetails){

        int uId = userDetails.getUserId();

        GetMyInfoResponse res = userService.getMyProfileInfo(uId);
        //log.info("res >>"+res);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //마이페이지-이력관리 수정
    @PatchMapping("/users/profile")
    @ResponseBody
    public ResponseEntity<?> updateMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody UpdateMyInfoRequest userReq) {
        try {
            int uId = userDetails.getUserId();

            String res = userService.updateMyProfileInfo(uId, userReq);
            //log.info("update myProfileInfo res>> " + res);
            if (res.equals("success")) {
                return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
            } else {
                throw new UserNotFoundException(res);
            }
        } catch (UserNotFoundException unfe) {
            log.error(unfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(unfe.getMessage());
        }
    }

    //마이페이지-개인정보 조회
    @GetMapping("/users/setting")
    @ResponseBody
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        int uId = userDetails.getUserId();
        //log.info("myinfo 조회 userId 찾기1  >>"+uId);

        GetMyInfoResponse res = userService.getMyInfo(uId);
        //log.info("get myInfo res>> "+res);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //마이페이지-개인정보 수정
    @PutMapping("/users/setting")
    @ResponseBody
    public ResponseEntity<?> updateMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody UpdateMyInfoRequest req){
        try {
            int uId = userDetails.getUserId();

            String res = userService.updateMyInfo(uId, req);
            log.info("update myProfileInfo res");
            if (res.equals("success")) {
                return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
            } else {
                throw new UserNotFoundException(res);
            }
        }catch (UserNotFoundException unfe){
            log.error(unfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(unfe.getMessage());
        }
    }

    //내가 작성한 글 목록 조회
    @GetMapping("/users/mymeeting")
    @ResponseBody
    public ResponseEntity<?> getMyMeeingList(@AuthenticationPrincipal CustomUserDetails userDetails){
        try {
            int uId = userDetails.getUserId();

            List<GetMyMeetingListResponse> res = meetingService.getMyMeetingList(uId);
            log.info("get mymeeting res >>" + res);

            if (res == null) {
                throw new MeetingNotFoundException("글이 없습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }catch (MeetingNotFoundException mnfe){
            log.error(mnfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mnfe.getMessage());
        }
    }

    //내가 작성한 글 목록 카테고리별/모집상태별 조회
    //@GetMapping(value = "/users/mymeetingfilter/{id}", produces = "application/json; charset=utf-8")
    @GetMapping(value = "/users/mymeetingfilter")
    public ResponseEntity<?> getMyMeetingListByFilter(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @RequestParam(name = "category", required = false) String strCategory,
                                                      @RequestParam(name="status", required = false) String strStatus){
        try {
            int uId = userDetails.getUserId();

            List<GetMyMeetingListResponse> res = meetingService.getMyMeetingListByFilter(uId, strCategory, strStatus);
            //log.info("get mymeetinglist by filter res >>"+res);

            if (res == null) {
                throw new MeetingNotFoundException("글이 없습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }catch (MeetingNotFoundException mnfe){
            log.error(mnfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mnfe.getMessage());
        }
    }

    //내가 신청한 글 목록 조회
    @GetMapping("/users/myapplying")
    @ResponseBody
    public ResponseEntity<?> getMyApplyingList(@AuthenticationPrincipal CustomUserDetails userDetails){
        try {
            int uId = userDetails.getUserId();

            List<GetMyApplyingListResponse> res = meetingService.getMyApplyingList(uId);
            //log.info("get applying res >>" + res);
            if (res == null) {
                throw new MeetingNotFoundException("글이 없습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }catch (MeetingNotFoundException mnfe){
            log.error(mnfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mnfe.getMessage());
        }
    }

    //내가 신청한 글 목록 카테고리별/모집상태별 조회
    @GetMapping(value = "/users/myapplyingfilter")
    public ResponseEntity<?> getMyApplyingListByFilterr(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestParam(name = "category", required = false) String strCategory,
                                                        @RequestParam(name="status", required = false) String strStatus){
        try {
            int uId = userDetails.getUserId();

            List<GetMyApplyingListResponse> res = meetingService.getMyAppyingListByFilter(uId, strCategory, strStatus);
            //log.info("get applying filter res >>" + res);
            if (res == null || res.isEmpty()) {
                throw new MeetingNotFoundException("글이 없습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }catch (MeetingNotFoundException mnfe){
            log.error(mnfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mnfe.getMessage());
        }
    }

    //프로필 이미지 수정
    @PutMapping(value = "/users/setting/img", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<?> updateMyImg(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestPart(name = "profilePhoto") MultipartFile file){
        try {
            int uId = userDetails.getUserId();

            UserImgFileDTO filedto = new UserImgFileDTO(file);
            String res = userService.updateMyProfileImg(uId, filedto);
            //log.info(res);
            if (res.equals("success")) {
                return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
            } else {
                throw new UserNotFoundException(res);
            }
        }catch (UserNotFoundException unfe){
            log.error(unfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(unfe.getMessage());
        }
    }


}
