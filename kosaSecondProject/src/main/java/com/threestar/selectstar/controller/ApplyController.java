package com.threestar.selectstar.controller;

import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.domain.service.ApplyService;
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
    public ResponseEntity<ApplyCheckResponse> checkByUserIdAndMeetingId(@RequestParam int meetingId,@AuthenticationPrincipal CustomUserDetails userDetails){
        try {
            return ResponseEntity.ok()
                    .body(applyService.checkApply(userDetails.getUserId(),meetingId)); // 신청 없으면 에러

        } catch (Exception e){
            return ResponseEntity.ok()
                    .body(null);
        }
     }
    @GetMapping("/user")
    public ResponseEntity<List<FindApplyByUserIdResponse>> applyListByUserId(@RequestParam int userId){
        return ResponseEntity.ok()
                .body(applyService.findApplyByUserId(userId));
    }
    @GetMapping("/meeting")
    public ResponseEntity<List<FindApplyByMeetingIdResponse>> applyListByMeetingId(@RequestParam(name = "meetingId") int meetingId){
        return ResponseEntity.ok()
                .body(applyService.findApplyByMeetingId(meetingId));
    }
    @GetMapping("/meeting/valid")
    public ResponseEntity<List<FindApplyByMeetingIdValidResponse>> applyListByMeetingIdValid(@RequestParam int meetingId){
        return ResponseEntity.ok()
                .body(applyService.findApplyByMeetingIdValid(meetingId));
    }
    @PostMapping
    public Map<String,String> applyAdd(@RequestBody ApplyRequest applyRequest,@AuthenticationPrincipal CustomUserDetails userDetails){
        Map<String, String> succesMap = new HashMap<>();
        applyRequest.setUserId(userDetails.getUserId());
        succesMap.put("result",applyService.addApply(applyRequest));
        return succesMap;
    }
    @PatchMapping("/reject")
    public Map<String,String> rejectApply(@RequestBody RejectApplyRequest rejectApplyRequest,@AuthenticationPrincipal CustomUserDetails userDetails){
        Map<String, String> succesMap = new HashMap<>();
        succesMap.put("result",applyService.rejectApplyByUserIdAndMeetingId(rejectApplyRequest));
        return succesMap;
    }
}
