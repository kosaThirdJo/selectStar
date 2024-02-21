package com.threestar.selectstar.controller;

import com.threestar.selectstar.dto.comment.response.GetCommentsResponse;
import com.threestar.selectstar.dto.comment.request.UpdateDeletedStatusRequest;
import com.threestar.selectstar.dto.meeting.response.GetMeetingsResponse;
import com.threestar.selectstar.dto.meeting.request.UpdateDeletedRequest;
import com.threestar.selectstar.dto.user.request.UpdateUserStatusRequest;
import com.threestar.selectstar.dto.user.response.GetUsersListResponse;
import com.threestar.selectstar.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 회원 전체 조회
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<GetUsersListResponse> users = adminService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // 회원 상태 변경
    @PatchMapping("/users")
    public ResponseEntity<?> updateUserStatus(@RequestBody UpdateUserStatusRequest request) {
        adminService.updateUserStatus(request.getUserId(), request.getUserStatus());
        return ResponseEntity.ok().build();
    }

    // 모임글 전체 조회
    @GetMapping("/meetings")
    public ResponseEntity<?> getAllMeetings() {
        List<GetMeetingsResponse> meetings = adminService.getAllMeetings();
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    // 모임글 삭제
    @PatchMapping("/meetings/delete")
    public ResponseEntity<?> deleteMeeting(@RequestBody UpdateDeletedRequest request) {
        adminService.deleteMeeting(request.getMeetingId(), request.getDeleted());
        return ResponseEntity.ok().build();
    }

    // 모임글 댓글 전체 조회
    @GetMapping("/meetings/comments")
    public ResponseEntity<?> getAllMeetingsComments() {
        List<GetCommentsResponse> comments = adminService.getAllComments();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // 모임글 댓글 삭제
    @PatchMapping("/meetings/comments/delete")
    public ResponseEntity<?> deleteComment(@RequestBody UpdateDeletedStatusRequest request) {
        adminService.deleteComment(request.getCommentId(), request.getDeleted());
        return ResponseEntity.ok().build();
    }
}
