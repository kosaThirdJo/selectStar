package com.threestar.selectstar.controller;

import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.service.ApplyService;
import com.threestar.selectstar.dto.apply.request.ApplyRequest;
import com.threestar.selectstar.dto.apply.request.RejectApplyRequest;
import com.threestar.selectstar.dto.apply.response.ApplyCheckResponse;
import com.threestar.selectstar.dto.apply.response.FindApplyByMeetingIdResponse;
import com.threestar.selectstar.dto.apply.response.FindApplyByMeetingIdValidResponse;
import com.threestar.selectstar.dto.apply.response.FindApplyByUserIdResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(originPatterns = {"*"})
@RestController
@RequestMapping("/apply")
public class ApplyController {

    ApplyService applyService;

    public ApplyController(ApplyService applyService) {
        this.applyService = applyService;
    }
    @GetMapping("/check")
    public ResponseEntity<?> checkByUserIdAndMeetingId(@RequestParam Long meetingId,@AuthenticationPrincipal CustomUserDetails userDetails){
        try {
            return ResponseEntity.ok()
                    .body(applyService.checkApply(userDetails.getUserId(),meetingId)); // 신청 없으면 에러

        } catch (Exception e){
            return ResponseEntity.status(204)
                    .build();
        }
     }
    @GetMapping("/user")
    public ResponseEntity<?> applyListByUserId(@RequestParam int userId){
        return ResponseEntity.ok()
                .body(applyService.findApplyByUserId(userId));
    }
    @GetMapping("/meeting")
    public ResponseEntity<?> applyListByMeetingId(@RequestParam(name = "meetingId") Long meetingId){
        return ResponseEntity.ok()
                .body(applyService.findApplyByMeetingId(meetingId));
    }
    @GetMapping("/meeting/valid")
    public ResponseEntity<?> applyListByMeetingIdValid(@RequestParam Long meetingId){
        return ResponseEntity.ok()
                .body(applyService.findApplyByMeetingIdValid(meetingId));
    }
    @PostMapping
    public ResponseEntity<?> applyAdd(@RequestBody ApplyRequest applyRequest,@AuthenticationPrincipal CustomUserDetails userDetails){
        applyRequest.setUserId(userDetails.getUserId());
        return ResponseEntity.ok()
                .body(applyService.addApply(applyRequest));
    }
    @PatchMapping("/reject")
    public ResponseEntity<?> rejectApply(@RequestBody RejectApplyRequest rejectApplyRequest,@AuthenticationPrincipal CustomUserDetails userDetails){
        rejectApplyRequest.setUserId(userDetails.getUserId());
        applyService.rejectApply(rejectApplyRequest);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/recognize")
    public ResponseEntity<?> recognizeApply(@RequestBody RejectApplyRequest rejectApplyRequest,@AuthenticationPrincipal CustomUserDetails userDetails){
        rejectApplyRequest.setUserId(userDetails.getUserId());
        applyService.recognizeApply(rejectApplyRequest);
        return ResponseEntity.ok().build();
    }
}
