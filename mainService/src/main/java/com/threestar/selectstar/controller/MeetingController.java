package com.threestar.selectstar.controller;

import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.config.jwt.JwtService;
import com.threestar.selectstar.service.MeetingService;
import com.threestar.selectstar.service.UserService;
import com.threestar.selectstar.dto.meeting.request.AddUpdateMeetingRequest;
import com.threestar.selectstar.dto.meeting.request.CompleteRequest;
import com.threestar.selectstar.dto.meeting.request.FindMainPageRequest;
import com.threestar.selectstar.dto.meeting.response.FindMainPageResponse;
import com.threestar.selectstar.dto.meeting.response.FindMeetingOneResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/meeting")
public class MeetingController {
    final MeetingService meetingService;
    final UserService userService;
    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public MeetingController(MeetingService meetingService, UserService userService, JwtService jwtService) {
        this.meetingService = meetingService;
        this.userService = userService;
    }


    // 전체 조회(페이징)
    @GetMapping
    public ResponseEntity<?> meetingList(FindMainPageRequest findMainPageRequest){
        return ResponseEntity.ok()
                .body(meetingService.findMainPage(findMainPageRequest));
    }
    // 단건 조회
    // TODO 만약 비어 있을경우 상태 처리
    @GetMapping("/{id}")
    public ResponseEntity<?> meetingDetail(@PathVariable("id") Long id,@AuthenticationPrincipal CustomUserDetails userDetails){

        FindMeetingOneResponse meetingOne = meetingService.findMeetingOne(id);

        meetingOne.setImg(userService.getUserProfile(meetingOne.getUserId()).getProfilePhoto());
        if (userDetails != null){
        meetingOne.setLoginId(userDetails.getUserId());
        }
        return ResponseEntity.ok()
                .body(meetingOne);
    }
    // 등록
    // TODO 만약 유효하지 않은 등록일 경우 상태 처리
    @PostMapping
    public ResponseEntity<?> meetingAdd(
            @RequestBody AddUpdateMeetingRequest addUpdateMeetingRequest,
                                         @AuthenticationPrincipal CustomUserDetails userDetails){

        int error = 0;
        addUpdateMeetingRequest.setUserId(userDetails.getUserId());
        for (ConstraintViolation<AddUpdateMeetingRequest> addUpdateMeetingRequestConstraintViolation : validator.validate(addUpdateMeetingRequest)) {
            System.out.println(addUpdateMeetingRequestConstraintViolation);
            error = 1;
        }
        if (error==1) {
            return ResponseEntity.status(400)
                    .build();
        }
        //TODO try catch 변경, void 변경
        meetingService.addMeeting( addUpdateMeetingRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
    //
    @PutMapping
    public ResponseEntity<?> meetingModify(@RequestBody AddUpdateMeetingRequest addUpdateMeetingRequest,@AuthenticationPrincipal CustomUserDetails userDetails){
        addUpdateMeetingRequest.setUserId(userDetails.getUserId());
        //TODO try catch 변경, void 변경
        meetingService.updateMeeting(addUpdateMeetingRequest);

        return ResponseEntity.ok()
                .body("수정 완료");
    }
    // 모집 완료
    // TODO 상태 코드 추가
    @PatchMapping("/complete")
    public ResponseEntity<?> meetingComplete(@RequestBody CompleteRequest completeRequest, @AuthenticationPrincipal CustomUserDetails userDetails){
        try {
            meetingService.completeMeeting(completeRequest);
            return ResponseEntity.ok()
                    .body("모집 완료 되었습니다.");
        } catch(Exception e) {
            return ResponseEntity.status(400)
                    .build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> meetingRemove
            (@AuthenticationPrincipal CustomUserDetails userDetails,
                    @PathVariable("id") Long meetingId){
        try {
            meetingService.removeMeeting(meetingId);
                    return ResponseEntity.ok()
                            .body("삭제 되었습니다.");
        } catch (Exception e){
            return ResponseEntity.status(400)
                    .build();
        }
    }
    @GetMapping("/bookmarking/{id}")
    public ResponseEntity<?> getBookmark(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("id") Long meetingId) {
        try {

            return ResponseEntity.ok()
                    .body(meetingService.isBookmark(meetingId,userDetails.getUserId()));
        } catch (Exception e){
            return ResponseEntity.status(400)
                    .build();
        }
}
    // 이미 북마크 데이터가 있을 경우 북마크 상태코드를 바꿔서 스위칭함
    @PatchMapping("/bookmaking/{id}")
    public ResponseEntity<?> meetingBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("id") Long meetingId) {
        try {
            meetingService.bookMarkingMeeting(meetingId, userDetails.getUserId());
            return ResponseEntity.ok()
                    .build();
        } catch (Exception e){
            return ResponseEntity.status(400)
                    .build();
        }
    }
    @GetMapping("/notification")
    public ResponseEntity<?> getNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        try {
            return  ResponseEntity.ok()
                    .body(meetingService.notificationPage(userDetails.getUserId()));
        } catch (Exception e){
            return ResponseEntity.status(400)
                    .build();
        }
    }
    @DeleteMapping("/notification/{notificationId}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable Long notificationId){
        try {meetingService.removeNotification(notificationId);
            return  ResponseEntity.ok()
                    .body("success");
        } catch (Exception e){
            return ResponseEntity.status(400)
                    .build();
        }
    }
}
