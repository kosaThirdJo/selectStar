package com.threestar.selectstar.controller;

import com.threestar.selectstar.config.auth.CustomUserDetails;
import com.threestar.selectstar.dto.comment.request.UpdateCommentRequest;
import com.threestar.selectstar.service.CommentService;
import com.threestar.selectstar.service.MeetingService;
import com.threestar.selectstar.dto.comment.request.AddCommentRequest;
import com.threestar.selectstar.dto.comment.response.FindCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//TODO map => dto

@CrossOrigin(origins = "*")
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
    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<?> commentListByMeetingId(
            @PathVariable Long meetingId,
            @RequestParam(defaultValue = "0") int page
    ){
     return ResponseEntity.ok()
                        .body(commentService.findComment(meetingId, PageRequest.of(page,10)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> commentListByUserId(@PathVariable int userId, int page){
        // 구현 마이 페이지 에서
        return null;
    }
    // 댓글 등록
    // return new ResponseEntity<>(response, HttpStatus.CREATED) 로 수정 해야 함
    @PostMapping("/meeting/{meetingId}")
    public ResponseEntity<?> addComment(@PathVariable Long meetingId,@RequestBody AddCommentRequest addCommentRequest, @AuthenticationPrincipal CustomUserDetails userDetails){
        addCommentRequest.setUserId(userDetails.getUserId());
        addCommentRequest.setMeetingId(meetingId);
        commentService.addComment(addCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
    // 댓글 삭제
    @DeleteMapping("/meeting/{commentId}")
    public ResponseEntity<?> removeComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId) {
        commentService.removeComment(commentId);
        return ResponseEntity.ok()
                .build();
    }
    // 댓글 수정
    @PatchMapping("/meeting/{commentId}")
    public ResponseEntity<?> updateComment(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long commentId,
        @RequestBody UpdateCommentRequest updateCommentRequest){
        commentService.updateComment(commentId,updateCommentRequest.getContent());
        return ResponseEntity.ok().build();
        }
    }

