package com.threestar.selectstar.controller;

import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.dto.comment.request.UpdateCommentRequest;
import com.threestar.selectstar.service.CommentService;
import com.threestar.selectstar.service.MeetingService;
import com.threestar.selectstar.dto.comment.request.AddCommentRequest;
import com.threestar.selectstar.dto.comment.response.FindCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//TODO map => dto
@RestController
@RequestMapping("/comment")
public class CommentController {
    final
    MeetingService meetingService;
    final CommentService commentService;

    public CommentController(MeetingService meetingService, CommentService commentService) {
        this.meetingService = meetingService;
        this.commentService = commentService;
    }
    // => 삭제 상태 조회로 변경 할 것...
    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<Page<FindCommentResponse>> commentListByMeetingId(
            @PathVariable Long meetingId,
            @RequestParam(defaultValue = "0") int page,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.ok()
                        .body(commentService.findComment(meetingId, PageRequest.of(page,10)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<?>> commentListByUserId(@PathVariable int userId, int page){
        // 구현 마이 페이지 에서
        return null;
    }
    // 댓글 등록
    // return new ResponseEntity<>(response, HttpStatus.CREATED) 로 수정 해야 함
    @PostMapping("/meeting/{meetingId}")
    public Map<String, String> addComment(@PathVariable Long meetingId,@RequestBody AddCommentRequest addCommentRequest, @AuthenticationPrincipal CustomUserDetails userDetails){
        Map<String, String> succesMap = new HashMap<>();
        addCommentRequest.setUserId(userDetails.getUserId());
        addCommentRequest.setMeetingId(meetingId);
        succesMap.put("result",commentService.addComment(addCommentRequest));
        return succesMap;
    }
    // 댓글 삭제
    @DeleteMapping("/meeting/{commentId}")
    public Map<String,String> removeComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId) {
        Map<String, String> succesMap = new HashMap<>();
        succesMap.put("result",commentService.removeComment(commentId));
        return succesMap;
    }
    // 댓글 수정
    @PatchMapping("/meeting/{commentId}")
    public Map<String,String> updateComment(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long commentId,
        @RequestBody UpdateCommentRequest updateCommentRequest){
        Map<String, String> succesMap = new HashMap<>();
        succesMap.put("result",commentService.updateComment(commentId,updateCommentRequest.getContent()));
        return succesMap;
        }
    }

